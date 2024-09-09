package mul.cam.e.dto;

import lombok.*;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class QuizDto {
    private int seq;
    private String content;
    private String answer;
}
