package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleUserInfDto {
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private String email_verified;
    private String at_hash;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    private String locale;
    private String iat;
    private String exp;
    private String alg;
    private String kid;
    private String typ;
}
