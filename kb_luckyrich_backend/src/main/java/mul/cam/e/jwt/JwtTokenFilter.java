package mul.cam.e.jwt;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.security.SecurityUser;
import mul.cam.e.security.SecurityUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUserService securityUserService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, SecurityUserService securityUserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityUserService = securityUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        String id = null;

        // /api 엔드포인트를 제외
        String requestURI = request.getRequestURI();
        log.info(requestURI);
        if (requestURI.startsWith("/api") || requestURI.startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 존재시
        if (token != null) {
            try {
                id = jwtTokenProvider.getId(token);
            } catch (Exception e) {
                // 토큰이 유효하지 않음
                log.info("***** 유효하지 않은 토큰 " + new Date());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        // 사용자 이메일이 존재하고 인증되지 않은 경우
        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 사용자 정보를 로드
            SecurityUser customUserDetail = securityUserService.loadUserByUsername(id);
            log.info(customUserDetail.getAuthorities().toString());
            // 토큰이 유효한 경우
            if (jwtTokenProvider.validateToken(token)) {
                // 인증 토큰을 생성하고 SecurityContext에 저장
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, null, customUserDetail.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // 토큰이 유효하지 않은 경우 401 에러 반환
                log.info("**** 유효하지 않은 토큰 " + new Date());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        } else if (id == null) {
            // 토큰이 없는 경우 401 에러 반환
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is missing");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // request에서 토큰이 제대로 들어왔는지 확인
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
