package mul.cam.e.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private int userId;
    private String userName;
    private String nickName;
    private String email;
    private String gender;
    private int age;
    private String role;
    private Integer realestateNo;
    private Integer carNo;
}