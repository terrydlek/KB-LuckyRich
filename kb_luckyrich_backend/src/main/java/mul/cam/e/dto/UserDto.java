package mul.cam.e.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private int id;
    private String user_name;
    private String email;
    private String gender;
    private int age;
}
