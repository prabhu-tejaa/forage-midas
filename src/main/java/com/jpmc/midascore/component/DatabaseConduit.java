package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Transactional
    public void process(Transaction transaction, float incentiveAmount) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            // 1. Deduct ONLY the transaction amount from the sender
            sender.setBalance(sender.getBalance() - transaction.getAmount());

            // 2. Add transaction amount AND the incentive to the recipient
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // 3. Save updated users
            userRepository.save(sender);
            userRepository.save(recipient);

            // 4. Record the transaction with the incentive
            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
            transactionRecordRepository.save(record);
        }
    }
}