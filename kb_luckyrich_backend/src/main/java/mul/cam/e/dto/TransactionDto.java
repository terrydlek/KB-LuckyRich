package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private int accountId;
    private long amount;
    private String transactionType;
    private String category;
    private String description;
    private Timestamp transactionDate;
}
