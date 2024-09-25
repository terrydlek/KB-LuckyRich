package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDetailDto {
    private String stockName;
    private String currentPrice;
    private String prevClosingPrice;
    private String openingPrice;
    private String highPrice;
    private String upperLimitPrice;
    private String lowPrice;
    private String lowerLimitPrice;
    private String tradeVolume;
    private String tradeValue;
    private String chartImageUrl;
}
