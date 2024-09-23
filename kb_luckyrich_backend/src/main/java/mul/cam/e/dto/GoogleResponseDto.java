package mul.cam.e.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleResponseDto {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}