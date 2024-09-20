package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyAccountDto {
    private int accountId;
    private String accountName;
    private String accountNumber;
    private long balance;
    private String accountType;
}
