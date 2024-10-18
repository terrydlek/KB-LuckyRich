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

    boolean updateUserInfo(@Param("user_name") String email, @Param("request") Map<String, String> request);

    int getAccountNum(String userName);

    SecurityUser findByUsername(String email);

    int getUserId(String userName);

    String getNickname(String userName);

    boolean withdrawUser(int userId);

    String getUserRole(String userName);
    
    String getRefreshToken(String userName);

    void updateRefreshToken(@Param("userName") String userName, @Param("refreshToken") String refreshToken);
}
