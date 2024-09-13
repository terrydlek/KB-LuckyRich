package mul.cam.e.dao;

import mul.cam.e.dto.UserDto;
import mul.cam.e.security.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    int userCheck(String email);
    boolean register(UserDto userDto);

    UserDetail login(String email);
}
