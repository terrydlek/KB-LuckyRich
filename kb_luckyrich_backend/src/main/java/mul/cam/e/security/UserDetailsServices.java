package mul.cam.e.security;

import mul.cam.e.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServices implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServices(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetail user = userService.login(email);

        return user;
    }


    public UserDetail loadUserByEmailAndProvider(String email, int provider_id) throws UsernameNotFoundException {
        UserDetail user = userService.loginByEmailAndProvider(email, provider_id);
        System.out.println("**************"+user);
        return user;
    }
}



/*
@Service
public class SecurityService implements UserDetailsService {

    private final MemberService service;

    public SecurityService(MemberService service) {
        this.service = service;
    }

    // Security 사용자 인증 -> Database 설정
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("~ loadUserByUsername()");

        // DB로 접근 <- dto
        SecurityUser user = service.login(username);

        UserDetails build = null;
        try {
            User.UserBuilder userBuilder = User.withUsername(username).password(user.getPassword());
            userBuilder.authorities(user.getAuthorities());
            build = userBuilder.build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return build;
    }

}





*/






