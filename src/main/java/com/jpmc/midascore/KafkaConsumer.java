package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final DatabaseConduit databaseConduit;

    // Spring Boot automatically injects the DatabaseConduit here
    public KafkaConsumer(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
        System.out.println("KafkaConsumer bean created with DatabaseConduit");
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        // Instead of just printing, we now process the transaction!
        databaseConduit.process(transaction);
    }
}