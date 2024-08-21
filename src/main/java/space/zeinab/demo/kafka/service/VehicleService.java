package space.zeinab.demo.kafka.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.zeinab.demo.kafka.client.VehicleServiceClient;
import space.zeinab.demo.kafka.config.KafkaConfig;
import space.zeinab.demo.kafka.model.HostInfoDTO;
import space.zeinab.demo.kafka.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleService {

    @Value("${server.port}")
    private Integer port;

    private final StreamsStoreService streamsStoreService;
    private final StreamsHostMetaDataService streamsHostMetaDataService;
    private final VehicleServiceClient vehicleServiceClient;

    public List<Vehicle> getAllVehicles(String queryOtherHosts) {
        log.info("Fetching data from current instance");
        var localStore = streamsStoreService.getLocalStore(KafkaConfig.STORE_NAME);
        var vehiclesInLocalStore = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(localStore.all(), 0), false
                )
                .map(keyValue -> keyValue.value)
                .toList();

        log.info("Fetching data from remote instance");
        var vehiclesInRemoteStore = retrieveAllVehiclesInOtherHosts(null, Boolean.parseBoolean(queryOtherHosts));

        var allVehicles = new ArrayList<>(vehiclesInLocalStore);
        allVehicles.addAll(vehiclesInRemoteStore);

        return allVehicles;
    }

    public Vehicle getVehicle(String vehicleId, String queryOtherHosts) {
        var hostMetaDataForKey = streamsHostMetaDataService.getMetaDataForKey(KafkaConfig.STORE_NAME, vehicleId);
        log.info("host metadata : {}", hostMetaDataForKey);

        Vehicle vehicle = null;
        if (hostMetaDataForKey != null) {
            if (hostMetaDataForKey.port() == port) {
                log.info("Fetching data from current instance");
                var localStore = streamsStoreService.getLocalStore(KafkaConfig.STORE_NAME);
                vehicle = localStore.get(vehicleId);
            } else {
                log.info("Fetching data from remote instance");
                vehicle = retrieveAllVehiclesInOtherHosts(vehicleId, Boolean.parseBoolean(queryOtherHosts)).get(0);
            }
        }
        return vehicle;
    }

    private List<Vehicle> retrieveAllVehiclesInOtherHosts(String vehicleId, boolean queryOtherHosts) {
        var otherHosts = otherHosts();
        log.info("Other hosts : {} ", otherHosts);
        log.info("query Other Hosts : {}", queryOtherHosts);

        if (queryOtherHosts && otherHosts != null && !otherHosts.isEmpty()) {
            if (vehicleId == null) {
                return otherHosts.stream()
                        .map(vehicleServiceClient::retrieveAllVehicles)
                        .flatMap(List::stream)
                        .toList();
            } else {
                return otherHosts.stream()
                        .map(hostInfoDTO -> vehicleServiceClient.retrieveVehicle(hostInfoDTO, vehicleId))
                        .toList();
            }
        }

        return List.of();
    }

    private List<HostInfoDTO> otherHosts() {
        return streamsHostMetaDataService
                .getMetaData()
                .stream()
                .filter(hostInfoDTO -> hostInfoDTO.port() != port)
                .toList();
    }

}
