package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyEstateDto {
    private int realEstateId;
    private String realEstateName;
    private long realEstatePrice;
}
