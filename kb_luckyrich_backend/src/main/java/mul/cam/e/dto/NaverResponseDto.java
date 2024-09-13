package mul.cam.e.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NaverResponseDto {
    private String access_token;
    private int expires_in;
    private String grant_type;
    private String token_type;
    private String id_token;
    private String refresh_token;

}
