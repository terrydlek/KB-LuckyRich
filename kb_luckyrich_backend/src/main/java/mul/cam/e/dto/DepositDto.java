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
    private String 금융회사;
    private String 상품명;
    private String 세전이자율;
    private String 세후이자율;
    private String 세후이자;
    private String 최고우대금리;
    private String 가입제한여부;
    private String 이자계산방식;
}

