package mul.cam.e.dao;

import mul.cam.e.dto.UserDto;
import mul.cam.e.security.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserDao {
    int userCheck(String email);
    boolean register(UserDto userDto);

    UserDetail login(String email);

    UserDetail getUserByEmail(String email);

    boolean updateUserInfo(@Param("email") String email, @Param("request") Map<String, String> request);

    int getAccountNum(String email);
}
