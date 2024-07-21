package space.zeinab.demo.kafka.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import space.zeinab.demo.kafka.model.HostInfoDTO;
import space.zeinab.demo.kafka.model.Vehicle;

import java.util.List;

@RequiredArgsConstructor
@Component
public class VehicleServiceClient {

    private final WebClient webClient;

    public Vehicle retrieveVehicle(HostInfoDTO hostInfoDTO, String vehicleId) {
        var baseUrl = "http://" + hostInfoDTO.host() + ":" + hostInfoDTO.port();
        var url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/v1/vehicles/{vehicleId}")
                .queryParam("query_other_hosts", "false")
                .buildAndExpand(vehicleId)
                .toString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Vehicle.class)
                .block();
    }

    public List<Vehicle> retrieveAllVehicles(HostInfoDTO hostInfoDTO) {
        var baseUrl = "http://" + hostInfoDTO.host() + ":" + hostInfoDTO.port();
        var url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/v1/vehicles")
                .queryParam("query_other_hosts", "false")
                .build()
                .toString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Vehicle.class)
                .collectList()
                .block();
    }

}
