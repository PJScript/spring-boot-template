package com.example.demo.filter;


import com.example.demo.config.UserDetailsServiceImpl;
import com.example.demo.exception.GlobalErroCode;
import com.example.demo.utils.ConnectUtils;
import com.example.demo.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsServiceImpl customUserDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JWT Filter Start");
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            if (jwtTokenUtils.validate(token)) {
                Claims claims = jwtTokenUtils.parseClaims(token);
                if (!validateRequestContext(claims, request, response)) {
                    return;
                }

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            } else {
                log.warn("JWT validation failed");
                setErrorResponse(GlobalErroCode.TOKEN_EXPIRED, response);
                return;
            }
        }else{
            log.warn("토큰이 없거나 BEARER 토큰이 아닙니다.");
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 생성시 기록된 값과 요청할 때의 값이 다르면 인증 불가능
     * */
    private boolean validateRequestContext(Claims claims, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientIp = claims.get("ipv4", String.class);
        String clientOs = claims.get("os", String.class);
        String clientBrowser = claims.get("browser", String.class);
        String clientWebType = claims.get("webType", String.class);

        if (clientIp == null || clientIp.isEmpty() || !clientIp.equals(request.getRemoteAddr()) ||
                !clientOs.equals(ConnectUtils.getOs(request)) || !clientBrowser.equals(ConnectUtils.getBrowser(request)) ||
                !clientWebType.equals(ConnectUtils.getWebType(request))) {
            setErrorResponse(GlobalErroCode.TOKEN_USER_MISMATCH, response);
            return false;
        }
        return true;
    }

    private void setErrorResponse(GlobalErroCode errorCode, HttpServletResponse response) throws IOException {
        log.info(errorCode.getLogMessge());
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + errorCode.getCode() + "\", \"message\": \"" + errorCode.getMessage() + "\"}");
    }
}
