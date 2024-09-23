package mul.cam.e.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleResponseDto {
    private String accessToken;
    private int expiresIn;
    private String scope;
    private String tokenType;
    private String idToken;
}