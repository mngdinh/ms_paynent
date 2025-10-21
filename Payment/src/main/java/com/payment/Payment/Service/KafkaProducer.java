package com.payment.Payment.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final int partition = 0;

    //    @Value("${spring.kafka.producer.topic}")
    private final String topic = "payment";

    //send event voi key = tableID
    public void sendEvent(String tableID, Object object) {
        kafkaTemplate.send(topic, tableID, object);
        kafkaTemplate.flush();
        log.info("Sent kafka payment at match: {} to topic: {} with key: {}", object, topic, tableID);
    }

}
