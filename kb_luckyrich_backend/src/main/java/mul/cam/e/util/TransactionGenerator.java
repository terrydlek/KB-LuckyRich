package mul.cam.e.util;

import mul.cam.e.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TransactionGenerator {

    private final RandUtils randUtils;

    public TransactionGenerator(RandUtils randUtils) {
        this.randUtils = randUtils;
    }

    public TransactionDto generateRandomTransactionDto(int accountId) {
//        int accountId = accountId;
        long amount = (long) randUtils.getRandomTransactionAmount();
        String transactionType = randUtils.getRandomTransactionType();
        String category = "Category";
        String description = "Description";
        Timestamp transactionDate = randUtils.getRandomTransactionDate();

        return new TransactionDto(accountId, amount, transactionType, category, description, transactionDate);
    }
}