package mul.cam.e.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundDto {
    private String managementCompany;
    private String productName;
    private double oneAccu;
    private double threeAccu;
    private double sixAccu;
    private double twelveAccu;
    private int fundRank;
    private String fundType;
    private int advanceFee;
    private double terFee;
}
