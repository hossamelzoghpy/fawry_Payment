package com.fawry.fawrypayment.filter;


import com.fawry.fawrypayment.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt=request.getHeader(ApplicationConstants.JWT_HEADER);
        if(jwt!=null) {
            try {
                Environment env = getEnvironment();
                if (null != env) {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                            ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    if (null != secretKey) {
                        String newJwt = jwt.replace(ApplicationConstants.TOKEN_PREFIX, "");
                        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(newJwt).getPayload();
                        String username = String.valueOf(claims.get("username"));
                        List<GrantedAuthority> roles = extractAuthorities(claims);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                                (roles));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                }
            } catch (Exception e) {
                throw new BadRequestException("Invalid token");
            }
        }

        filterChain.doFilter(request, response);


    }

    public List<GrantedAuthority> extractAuthorities(Claims claims) {

        List<?> rolesList = claims.get("Roles", List.class);

        if (rolesList == null || rolesList.isEmpty()) {
            return new ArrayList<>();
        }
        return rolesList.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }
}
