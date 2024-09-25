package mul.cam.e.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
public class UserDto implements UserDetails {
    private int user_id;
    private String user_name;
    private String email;
    private String gender;
    private int age;
    private int provider_id;

    @Builder(toBuilder = true)
    public UserDto(String user_name, String email, String gender, int age, int provider_id) {
//        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.provider_id = provider_id;
    }

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
        return user_name;
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
