package mul.cam.e.security;

import lombok.AllArgsConstructor;
import mul.cam.e.config.WebConfig;
import mul.cam.e.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Date;

@Import(WebConfig.class)
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;        // 토큰 검사용
    private final UserDetailsServices userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // api login -> access token 없이 진입 가능
                        .antMatchers("/api/**").permitAll()
                        .antMatchers("/asset/**").permitAll()
                        .antMatchers("/test-redis").permitAll()
                        .antMatchers("/user/**").authenticated()
                        .antMatchers("/myasset/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin().disable()
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ; // ChannelProcessingFilter.class); // CORS 필터 추가

        http.sessionManagement((sessionManagement)
                -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .and()
                .build();
    }


    @Bean
    public CorsFilter corsFilter() {
        System.out.println("^^ SecurityConfig corsFilter " + new Date());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 쿠키나 인증 정보 허용
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 출처
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 메서드 허용 (GET, POST, PUT, DELETE, etc.)

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

    /*
    // 회원가입 시에 암호화할 클래스
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1.권한 filter
        http.csrf(csrf -> csrf.disable())
                 .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .antMatchers("/asset/**").permitAll()
                        .antMatchers("/api/**").permitAll()
                        .antMatchers("/asset/**").permitAll()
                        .anyRequest().authenticated());   // (무조건)허용

        // 2. 세션을 사용하지 않음
        http.sessionManagement((sessionManagement)
                ->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );

        // 3. JWT 필터를 사용여부 설정
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 4. login form


        // enable h2-console
        http.headers(headers ->
            headers.frameOptions(options ->
                    options.sameOrigin()  // SAME ORIGIN은 같은 도메인 내에서의 참조만 허용하겠다는 것
            )
        );

        // 5. csrf 설정을 off 합니다
        //http.csrf(csrf -> csrf.disable());

        return http.build();
    }
*/












