package mul.cam.e.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BbsDto {
    private int seq;
    private String userId;
    private String title;
    private String content;
    private Timestamp wdate;
    private int readcount;
}
