package mul.cam.e.dto;

import lombok.*;

import java.sql.Timestamp;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class AccountHistoryDto {
    private String accountId;
    private Timestamp date;
    private long amount;
    private String transactionType;
}
