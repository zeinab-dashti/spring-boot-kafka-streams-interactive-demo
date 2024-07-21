package space.zeinab.demo.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import space.zeinab.demo.kafka.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
public class VehicleMockDataProducer {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        var dummyData = generateDummyData();
        publishData(dummyData, objectMapper);
    }

    private static List<Vehicle> generateDummyData() {
        Random random = new Random();
        String[] vehicleIds = {"vehicle1", "vehicle2"};

        List<Vehicle> vehicleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (String vehicleId : vehicleIds) {
                Vehicle data = new Vehicle(
                        vehicleId,
                        random.nextDouble() * 180 - 90,// Random latitude
                        random.nextDouble() * 360 - 180,// Random longitude
                        random.nextDouble() * 100,// Random speed
                        random.nextDouble() * 100,// Random fuel level
                        System.currentTimeMillis()
                );

                vehicleList.add(data);
            }
        }

        return vehicleList;
    }

    private static void publishData(List<Vehicle> vehicleList, ObjectMapper objectMapper) {
        vehicleList
                .forEach(vehicle -> {
                    try {
                        var vehicleJSON = objectMapper.writeValueAsString(vehicle);
                        var recordMetaData = ProducerUtil.produceRecord(vehicle.vehicleId(), vehicleJSON);
                        log.info("Published vehicle data : {} ", recordMetaData);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
