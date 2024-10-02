package mul.cam.e.security;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dao.UserDao;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SecurityUserService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {

        SecurityUser user = userDao.findByUsername(username);
//        log.info("Found user: {}", user);

        /*
        if (user != null) {
            SecurityUser authUser = SecurityUser.builder()
                    .userName(user.getUsername())
                    .nickName(user.getNickName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .age(user.getAge())
                    .role(user.getRole())
                    .build();

            log.info("authUser : " + authUser);
            return authUser;
        }
        */

        return user;
    }

    public SecurityUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(SecurityUser securityUser) {
        userDao.register(securityUser);
    }

    public boolean updateUserInfo(String email, Map<String, String> request) {
        return userDao.updateUserInfo(email, request);
    }

    public int getAccountNum(String id) {
        return userDao.getAccountNum(id);
    }

    public int getUserId(String name) { return userDao.getUserId(name); }

    public String getNickname(String userName) {
        return userDao.getNickname(userName);
    }

    public int checkAuthority(String userName) {
        return userDao.checkAuthority(userName);
    }
}
