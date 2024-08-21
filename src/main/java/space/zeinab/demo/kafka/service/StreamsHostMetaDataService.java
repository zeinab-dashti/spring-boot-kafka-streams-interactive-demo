package space.zeinab.demo.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsMetadata;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;
import space.zeinab.demo.kafka.model.HostInfoDTO;
import space.zeinab.demo.kafka.model.HostInfoDTOWithKey;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StreamsHostMetaDataService {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public List<HostInfoDTO> getMetaData() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();

        if (kafkaStreams != null) {
            var streamsMetaData = kafkaStreams.metadataForAllStreamsClients();

            return streamsMetaData.stream()
                    .map(StreamsMetadata::hostInfo)
                    .map(hostInfo -> new HostInfoDTO(hostInfo.host(), hostInfo.port()))
                    .toList();
        } else {
            throw new IllegalStateException("Exception to get Kafka Streams");
        }

    }

    public HostInfoDTOWithKey getMetaDataForKey(String storeName, String key) {
        var kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();

        if (kafkaStreams != null) {
            var metaData = kafkaStreams.queryMetadataForKey(storeName, key, Serdes.String().serializer());

            if (metaData != null) {
                var activeHost = metaData.activeHost();
                return new HostInfoDTOWithKey(activeHost.host(), activeHost.port(), key);
            }
            return null;

        } else {
            throw new IllegalStateException("Exception to get Kafka Streams");
        }
    }

}
