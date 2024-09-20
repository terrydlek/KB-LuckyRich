package mul.cam.e.service;

import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.UserDto;
import mul.cam.e.security.UserDetail;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int userCheck(String email) {
        return userDao.userCheck(email);
    }

    public boolean register(UserDto userDto) {
        return userDao.register(userDto);
    }

    public UserDetail login(String email) {
        return userDao.login(email);
    }

    public UserDetail getUserByEmail(String email) { return userDao.getUserByEmail(email); }

    public boolean updateUserInfo(String email, Map<String, String> request) {
        try {
            return userDao.updateUserInfo(email, request);
        } catch (Exception e) {
            System.out.println("Error updating user info: " + e.getMessage());
            return false;
        }
    }
}
