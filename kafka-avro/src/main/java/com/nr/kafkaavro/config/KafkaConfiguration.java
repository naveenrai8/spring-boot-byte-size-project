package com.nr.kafkaavro.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, Object> kafkaStringValueProducerFactory() {
        Map<String, Object> kafkaConfigProperties = new HashMap<>();
        kafkaConfigProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaConfigProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        kafkaConfigProperties.put("schema.registry.url", "http://127.0.0.1:8081");
        return new DefaultKafkaProducerFactory<>(kafkaConfigProperties);
    }


    @Bean
    public ConsumerFactory<String, Object> kafkaStringValueConsumerFactory() {
        Map<String, Object> kafkaConfigProperties = commonConsumerConfig();
        kafkaConfigProperties.put("schema.registry.url", "http://127.0.0.1:8081");
        kafkaConfigProperties.put("specific.avro.reader", "true");
        kafkaConfigProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(kafkaConfigProperties);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaProducerStringValueTemplate() {
        return new KafkaTemplate<>(kafkaStringValueProducerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaStringValueListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaStringValueConsumerFactory());
        return factory;
    }

    private Map<String, Object> commonConsumerConfig() {
        Map<String, Object> kafkaConfigProperties = new HashMap<>();
        kafkaConfigProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return kafkaConfigProperties;
    }

    private Map<String, Object> commonProducerConfig() {
        Map<String, Object> kafkaConfigProperties = new HashMap<>();
        kafkaConfigProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return kafkaConfigProperties;
    }
}
