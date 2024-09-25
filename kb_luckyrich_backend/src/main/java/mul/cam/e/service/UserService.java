package mul.cam.e.service;

import mul.cam.e.dao.UserDao;
import mul.cam.e.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(SecurityUser securityUser) {
        userDao.register(securityUser);
    }

    public boolean updateUserInfo(String email, Map<String, String> request) {
        return userDao.updateUserInfo(email, request);
    }

    public int getAccountNum(String email) {
        return userDao.getAccountNum(email);
    }

    public SecurityUser getUserDtoByEmail(String email) { return userDao.getUserDtoByEmail(email); }

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserDtoByEmail(username);
    }
}
