package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestmentTestResultDto {
    private int id;
    private int userId;
    private String investmentType;
    private Timestamp testDate;
}
