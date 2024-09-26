package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private String imageUrl;     // 이미지 URL
    private String title;        // 뉴스 제목
    private String description;  // 뉴스 간단 내용
    private String url;
    private String code;
}
