package mul.cam.e.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String email;
    private String gender;
    private int age;
    private int provider_id;
}
