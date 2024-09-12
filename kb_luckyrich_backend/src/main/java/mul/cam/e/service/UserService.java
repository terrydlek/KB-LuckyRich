package mul.cam.e.service;

import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.UserDto;
import org.springframework.stereotype.Service;

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
}
