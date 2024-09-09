package mul.cam.e.dto;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String auth;
}
