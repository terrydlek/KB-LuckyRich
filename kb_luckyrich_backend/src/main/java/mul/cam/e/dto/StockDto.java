package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stockName;       // 종목명
    private String stockCode;       // 주식 코드
    private String stockPrice;      // 현재가
    private String comparePre;         // 전일비
    private String fluctuationRate;  // 등락률
    private String tradingVolume;      // 거래량
    private String marketCapitalization;  // 시가총액
    private String salesAmount;        // 매출액
    private String operatingProfit;   // 영업 이익
    private String earningsPerShare;   // 주당 순 이익
    private String per;
    private String roe;


}
