package mul.cam.e.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class GoalDto {
    private String userId;
    private String goal;
    private long goalAmount;
    private Timestamp startDate;
    private Timestamp endDate;

}
