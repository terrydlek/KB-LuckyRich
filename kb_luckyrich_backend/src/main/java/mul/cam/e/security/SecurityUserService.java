package mul.cam.e.security;

import mul.cam.e.dao.UserDao;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SecurityUserService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {

        SecurityUser user = userDao.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }

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
}
