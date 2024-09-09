package mul.cam.e.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@ToString
@Setter
@Getter
public class comment {
    private int commentSeq;
    private String userId;
    private String commentContent;
    private Timestamp wdate;
}
