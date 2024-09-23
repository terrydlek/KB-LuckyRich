package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private int transactionId;
    private int accountId;
    private long amount;
    private String transactionType;
    private String category;
    private String description;
    private Timestamp transactionDate;

    // transactionId를 제외한 생성자
    public TransactionDto(int accountId, long amount, String transactionType, String category, String description, Timestamp transactionDate) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.category = category;
        this.description = description;
        this.transactionDate = transactionDate;
    }
}
