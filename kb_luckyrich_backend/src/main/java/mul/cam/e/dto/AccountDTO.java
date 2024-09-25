package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private int accountId;
    private String accountNumber;
    private int userId;
    private int bankId;
    private int accountTypeId;
    private long balance;

    public AccountDto(String accountNumber, int bankId, int accountTypeId, long balance) {
        this.accountNumber = accountNumber;
        this.bankId = bankId;
        this.accountTypeId = accountTypeId;
        this.balance = balance;
    }
}
