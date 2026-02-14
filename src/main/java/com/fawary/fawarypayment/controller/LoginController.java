package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.constants.ApplicationConstants;
import com.fawary.fawarypayment.dto.LoginRequestDto;
import com.fawary.fawarypayment.dto.LoginResponseDto;
import com.fawary.fawarypayment.repo.UsersRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    @PostMapping("/loginApi")
    public ResponseEntity<LoginResponseDto> loginApi(@RequestBody LoginRequestDto loginRequestDto){
        String jwt="";
        Authentication authentication= UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDto.username()
                ,loginRequestDto.password());
        Authentication authenticationResponse=authenticationManager.authenticate(authentication);
        if(null!=authenticationResponse && authenticationResponse.isAuthenticated()){

            String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                    ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            jwt= Jwts.builder().issuer("fawary system").subject("token")
                    .claim("username",authentication.getName())
                    .claim("Roles",authentication.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                    .issuedAt(new java.util.Date())
                    .expiration(new java.util.Date((new java.util.Date()).getTime()+30000000))
                    .signWith(secretKey).compact();

        }
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,jwt)
                .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), jwt));
    }
}
