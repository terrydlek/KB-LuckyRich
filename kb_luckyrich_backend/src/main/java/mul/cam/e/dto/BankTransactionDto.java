package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransactionDto {
    private int accountId;
    private  String accountNumber;
    private long amount;
    private String transactionType;
    private String category;
    private String description;
    private Timestamp transactionDate;
    private int bankId;
}
