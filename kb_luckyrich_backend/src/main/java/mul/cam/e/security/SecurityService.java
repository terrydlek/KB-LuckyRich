package mul.cam.e.security;

import mul.cam.e.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    private final MemberService service;

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    public SecurityService(MemberService service) {
        this.service = service;
    }

    // Security 사용자 인증 -> Database 설정
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("~ loadUserByUsername()");

        // DB로 접근 <- dto
        SecurityUser user = service.login(username);

//        SecurityUser securityUser = new SecurityUser();
//        securityUser.setUsername("admin");
//        securityUser.setPassword("$2a$10$EsIMfxbJ6NuvwX7MDj4WqOYFzLU9U/lddCyn0nic5dFo3VfJYrXYC");

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











