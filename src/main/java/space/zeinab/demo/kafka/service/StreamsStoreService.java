package space.zeinab.demo.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;
import space.zeinab.demo.kafka.model.Vehicle;

@RequiredArgsConstructor
@Service
public class StreamsStoreService {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public ReadOnlyKeyValueStore<String, Vehicle> getLocalStore(String storeName) {
        var kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();

        if (kafkaStreams != null) {
            return kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType(
                            storeName,
                            QueryableStoreTypes.keyValueStore()
                    )
            );
        } else {
            throw new IllegalStateException("Exception to get Kafka Streams");
        }
    }

}
