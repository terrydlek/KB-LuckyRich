package mul.cam.e.dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class AccountDto {
    private String userId;
    private String accountId;
    private String accountNum;
    private long balance;
}
