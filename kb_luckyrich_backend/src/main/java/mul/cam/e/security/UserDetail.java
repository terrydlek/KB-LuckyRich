package mul.cam.e.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails {

    private String username;
    private String email;
    private String gender;
    private int age;
    private Collection<? extends GrantedAuthority> authorities;
//    private String auth;        // ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
//    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        auth.add(new SimpleGrantedAuthority("authority"));  // ROLE_USER, ROLE_ADMIN
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 계정만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정잠김여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용자 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
