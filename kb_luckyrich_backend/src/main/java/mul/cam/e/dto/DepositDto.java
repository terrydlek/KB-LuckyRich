package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositDto {
    private String company;
    private String prodname;
    private String prerate;
    private String afterrate;
    private String afterinterest;
    private String bestinterest;
    private String limit;
    private String calmethod;
}
