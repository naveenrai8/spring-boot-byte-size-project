package com.nr.kafkaavro.consumer;

import com.nr.kafkaavro.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KafkaAvroConsumer {

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "${app.kafka.group-id}",
            containerFactory = "kafkaStringValueListenerContainerFactory"
    )
    public void avroConsumer(Employee employee) {
        log.info("Received Employee: {}", employee);
    }

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "${app.kafka.group-id}-another"
    )
    public void avroAnotherConsumer(ConsumerRecord<String, Employee> record, @Headers Map<String, Object> headers) {
        log.info("Received record key: {}", record.key());
        log.info("Received record value: {}", record.value());
        for (Header header : record.headers()) {
            log.info("Received record header key: {}, value: {} ", header.key(), header.value());
        }

        headers.forEach(
                (key, value) -> log.info("Received key: {}, value: {}", key, value)
        );
    }
}
