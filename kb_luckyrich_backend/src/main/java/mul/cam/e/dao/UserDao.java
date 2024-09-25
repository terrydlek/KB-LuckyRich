package mul.cam.e.dao;

import mul.cam.e.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserDao {
    boolean register(UserDto userDto);

    UserDto login(String email);

//    UserDetail getUserByEmail(String email);
    UserDto getUserByEmail(@Param("params") Map<String, Object> params);

    boolean updateUserInfo(@Param("email") String email, @Param("request") Map<String, String> request);

    int getAccountNum(String email);

    UserDto getUserDtoByEmail(String email);
}
