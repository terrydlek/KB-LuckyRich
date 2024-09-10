package mul.cam.e.service;


import mul.cam.e.security.SecurityUser;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public SecurityUser login(String username){
        return new SecurityUser();
    }
}
