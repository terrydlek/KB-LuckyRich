package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardReplyDto {
    private int replyNum;
    private int boardNum;
    private String reply;
    private Timestamp replyAt;
}
