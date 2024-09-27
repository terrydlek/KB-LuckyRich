package mul.cam.e.security;

import lombok.*;
import mul.cam.e.util.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    private int userId;
    private String userName;
    private String nickName;
    private String email;
    private String gender;
    private int age;
    private Role role;

    @Builder()
    public SecurityUser(String userName, String nickName, String email, String gender, int age) {
//        this.user_id = user_id;
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.gender = gender;
        this.age = age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

//        System.out.println("Role : " + role);

        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userName;
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
