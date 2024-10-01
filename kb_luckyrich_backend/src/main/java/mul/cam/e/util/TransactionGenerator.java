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
        String category = getCategoryBasedOnTransactionType(transactionType);
        String description = "Description";
        Timestamp transactionDate = randUtils.getRandomTransactionDate();

        return new TransactionDto(accountId, amount, transactionType, category, description, transactionDate);
    }

    private String getCategoryBasedOnTransactionType(String transactionType) {
        if ("입금".equals(transactionType)) {
            return getRandomCategory(new String[]{"급여", "아르바이트", "용돈", "금융수입", "기타"});
        } else if ("출금".equals(transactionType)) {
            return getRandomCategory(new String[]{"식비", "카페", "술", "쇼핑", "교통", "여가", "여행", "경조"});
        }
        return "기타";
    }

    private String getRandomCategory(String[] categories) {
        int index = randUtils.random.nextInt(categories.length);
        return categories[index];
    }
}