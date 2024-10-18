package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 펀드 데이터를 표현하는 Data Transfer Object 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundDto {
//    private String managementCompany;
//    private String productName;
//    private double oneAccu;
//    private double threeAccu;
//    private double sixAccu;
//    private double twelveAccu;
//    private int fundRank;
//    private String fundType;
//    private int advanceFee;
//    private double terFee;

    private String name;
    private String url;
    private String symbol;
    private String lastPrice;
    private String changePercent;
    private String totalAssets;
    private String lastUpdate;
    private String country;
    private String issuer;
    private int riskRating;
    private String assetClass;
}
