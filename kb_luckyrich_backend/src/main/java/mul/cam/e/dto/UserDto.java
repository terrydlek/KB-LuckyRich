package mul.cam.e.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
public class UserDto {
    private int user_id;
    private String user_name;
    private String email;
    private String gender;
    private int age;
    private int provider_id;

    @Builder(toBuilder = true)
    public UserDto(String user_name, String email, String gender, int age, int provider_id) {
//        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.provider_id = provider_id;
    }
}
