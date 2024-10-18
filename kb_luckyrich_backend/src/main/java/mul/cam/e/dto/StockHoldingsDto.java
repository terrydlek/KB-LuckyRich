package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockHoldingsDto {
    private int holdingId;
    private int userId;
    private String stockSymbol;
    private String stockName;
    private int purchasePrice;
    private int quantity;
    private Date purchaseDate;

    @Builder
    public StockHoldingsDto(int userId, String stockSymbol, String stockName, int purchasePrice, int quantity, Date purchaseDate) {
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }
}
