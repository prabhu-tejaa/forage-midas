package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:8080/incentive";

    public Incentive getIncentive(Transaction transaction) {
        // Spring handles the serialization of the Transaction object automatically
        return restTemplate.postForObject(url, transaction, Incentive.class);
    }
}