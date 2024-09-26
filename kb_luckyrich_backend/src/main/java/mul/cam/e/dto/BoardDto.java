package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    private long id;
    private int userId;
    private String title;
    private String content;
    private String author; // 작성자 name
    private String createdAt;
}
