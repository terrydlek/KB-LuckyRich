package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyStockDto {
    private int investId;
    private String investSymbol;
    private Timestamp investDate;
    private long investPrice;

}
