package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KafkaConsumer {
    private final DatabaseConduit databaseConduit;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String incentiveApiUrl = "http://localhost:8080/incentive";

    public KafkaConsumer(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        // 1. Validate and process the transaction first
        // 2. Call the Incentive API
        Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);

        // 3. Pass both to the conduit to update balances correctly
        if (incentive != null) {
            databaseConduit.process(transaction, incentive.getAmount());
        }
    }
}