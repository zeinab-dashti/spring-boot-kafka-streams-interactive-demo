package space.zeinab.demo.kafka.topology;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;
import space.zeinab.demo.kafka.config.KafkaConfig;
import space.zeinab.demo.kafka.model.Vehicle;

@Component
public class VehicleStatusTopology {

    private final ObjectMapper objectMapper;

    public VehicleStatusTopology(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {
        var vehicleInput = streamsBuilder.stream(
                KafkaConfig.INPUT_TOPIC,
                Consumed.with(Serdes.String(), getVehicleSerde())
        );

        vehicleInput.print(Printed.<String, Vehicle>toSysOut().withLabel("* Input stream *"));

        var vehicleLastStatus = vehicleInput.groupByKey(Grouped.with(Serdes.String(), getVehicleSerde()))
                .reduce(
                        (oldValue, newValue) -> newValue,
                        Materialized.<String, Vehicle, KeyValueStore<Bytes, byte[]>>as(KafkaConfig.STORE_NAME)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(getVehicleSerde())
                );

        vehicleLastStatus.toStream().print(Printed.<String, Vehicle>toSysOut().withLabel("* Processed stream *"));

        vehicleLastStatus.toStream().to(
                KafkaConfig.OUTPUT_TOPIC,
                Produced.with(Serdes.String(), getVehicleSerde())
        );
    }

    private JsonSerde<Vehicle> getVehicleSerde() {
        return new JsonSerde<>(Vehicle.class, objectMapper);
    }

}
