package mul.cam.e.dao;

import mul.cam.e.security.SecurityUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserDao {
    boolean register(SecurityUser securityUser);

    boolean updateUserInfo(@Param("email") String email, @Param("request") Map<String, String> request);

    int getAccountNum(String email);

    SecurityUser getUserDtoByEmail(String email);
}
