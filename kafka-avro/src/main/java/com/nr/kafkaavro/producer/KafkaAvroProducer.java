package com.nr.kafkaavro.producer;

import com.nr.kafkaavro.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class KafkaAvroProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.topic}")
    private String topicName;

        public KafkaAvroProducer(@Qualifier("kafkaProducerStringValueTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
//    public KafkaAvroProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        log.info("++++++++++++++++++++++++++++++++++++++++++");
        log.info(kafkaTemplate.getProducerFactory().getConfigurationProperties().toString());
        kafkaTemplate.getProducerFactory().getConfigurationProperties().forEach((key, value) -> {
            log.info("{}: {}", key, value.toString());
        });
    }

    public void sendMessage(Employee employee) {
        employee.setId(UUID.randomUUID().toString());
        this.kafkaTemplate.send(MessageBuilder
                .withPayload(employee)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.KEY, "Key_" + UUID.randomUUID())
                .build());
    }
}
