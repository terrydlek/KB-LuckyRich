package mul.cam.e.service;

import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
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

    public void register(UserDto userDto) {
        userDao.register(userDto);
    }

    public UserDto login(String email) {
        return userDao.login(email);
    }

//    public UserDetail getUserByEmail(String email) { return userDao.getUserByEmail(email); }

    public boolean updateUserInfo(String email, Map<String, String> request) {
        return userDao.updateUserInfo(email, request);
    }

    public int getAccountNum(String email) {
        return userDao.getAccountNum(email);
    }

    public UserDto getUserByEmail(Map<String, Object> params) {
        return userDao.getUserByEmail(params);
    }

    public UserDto getUserDtoByEmail(String email) { return userDao.getUserDtoByEmail(email); }

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserDtoByEmail(username);
    }
}
