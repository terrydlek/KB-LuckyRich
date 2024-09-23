package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockCompareDto {
    private List<String> stockNames;
    private List<String> currentPrices;
    private List<String> priceChanges;
    private List<String> fluctuationRates;
    private List<String> marketCaps;
    private List<String> foreignOwnerships;
    private List<String> sales;
    private List<String> operatingProfits;
    private List<String> netProfits;
    private List<String> eps;
    private List<String> roes;
    private List<String> pers;
    private List<String> pbrs;
}
