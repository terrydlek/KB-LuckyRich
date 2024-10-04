package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCarDto {
    private int carNo;
    private String carModel;
    private long carPrice;
}
