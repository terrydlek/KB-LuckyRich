package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private int boardNum;
    private String userName;
    private String nickName;
    private String title;
    private String content;
    private Timestamp createdAt;
}
