package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockTimeDto {
    private String nowVal;      // 현재가
    private String diff;        // 전일대비
    private String rate;        // 등락률(%)
    private String quant;       // 거래량
    private String amount;      // 거래대금(백만)
    private String faceVal;     // 액면가
    private String upperLimit;  // 상한가
    private String lowerLimit;  // 하한가
    private String per;         // PER
    private String eps;         // EPS
    private String high52Week;  // 52주 최고
    private String low52Week;   // 52주 최저
    private String marketCap;   // 시가총액
    private String foreignShares; // 외국인현재
    private String sellingPrice;  // 매도호가
    private String buyingPrice;   // 매수호가
    private String previousPrice; // 전일가
    private String openingPrice;  // 시가
    private String highPrice;     // 고가
    private String lowPrice;      // 저가
    private String previousUpperLimit; // 전일상한
    private String previousLowerLimit; // 전일하한
    private String listedShares;  // 상장주식수
    private String capital;       // 자본금
}
