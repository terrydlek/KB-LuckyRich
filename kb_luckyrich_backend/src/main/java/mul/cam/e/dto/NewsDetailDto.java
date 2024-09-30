package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDetailDto {
    private String title;
    private String dateTime;
    private String imageUrl;
    private String articleText;
    private String imageDescription;
}
