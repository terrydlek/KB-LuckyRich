package mul.cam.e.util;

import mul.cam.e.dto.TransactionDto;
import mul.cam.e.enumrate.TransactionType;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TransactionGenerator {

    private final RandUtils randUtils;

    public TransactionGenerator(RandUtils randUtils) {
        this.randUtils = randUtils;
    }

    public TransactionDto generateRandomTransactionDto(int accountId) {
        long amount = (long) randUtils.getRandomTransactionAmount();
        String transactionType = randUtils.getRandomTransactionType();
        String category = getCategoryBasedOnTransactionType(transactionType);
        String description = "Description";
        Timestamp transactionDate = randUtils.getRandomTransactionDate();

        return new TransactionDto(accountId, amount, transactionType, category, description, transactionDate);
    }

    private String getCategoryBasedOnTransactionType(String transactionType) {
        TransactionType type = TransactionType.valueOf(transactionType);
        return getRandomCategory(type.getCategories());
    }

    private String getRandomCategory(String[] categories) {
        int index = randUtils.random.nextInt(categories.length);
        return categories[index];
    }
}


// transaction amount 1-2
// car