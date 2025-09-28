package com.nr.kafkaavro.controller;

import com.nr.kafkaavro.dto.Employee;
import com.nr.kafkaavro.producer.KafkaAvroProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final KafkaAvroProducer kafkaAvroProducer;

    @PostMapping
    public void sendMultipleMessages(@RequestBody Employee employee) {
        log.info("Sending records to Kafka" + employee);
            this.kafkaAvroProducer.sendMessage(employee);
    }
}
