package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateDto {
    private int no;
    private String city;
    private String streetNumber;
    private String estateName;
    private String exclusiveArea;
    private String contractTime;
    private String transactionAmount;
    private String floor;
    private String buyer;
    private String bargainer;
    private String constructionTime;
    private String streetName;
    private String transactionType;
    private String brokerageLocation;
    private String houseType;
}
