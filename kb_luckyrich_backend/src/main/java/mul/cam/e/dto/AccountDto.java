package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mul.cam.e.util.KeyDecrypt;

// 최신 AccountDto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private int accountId;
    private String accountNumber;
    private int userId;
    private int bankId;
    private int accountTypeId;
    private int balance;

    @Builder
    public AccountDto(String accountNumber, int userId, int bankId, int accountTypeId, int balance) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.bankId = bankId;
        this.accountTypeId = accountTypeId;
        this.balance = balance;
    }

    public String getDecryptedAccountNumber(KeyDecrypt keyDecrypt) {
        return keyDecrypt.decrypt(this.accountNumber);
    }
}