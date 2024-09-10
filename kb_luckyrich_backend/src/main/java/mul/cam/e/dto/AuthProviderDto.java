package mul.cam.e.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthProviderDto {
    private int id;
    private String provider_name;
}
