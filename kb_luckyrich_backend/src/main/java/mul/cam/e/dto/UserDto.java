package mul.cam.e.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String user_name;
    private String email;
    private String gender;
    private int age;
}
