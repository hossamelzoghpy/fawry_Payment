package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.constants.ApplicationConstants;
import com.fawary.fawarypayment.dto.LoginRequestDto;
import com.fawary.fawarypayment.dto.LoginResponseDto;
import com.fawary.fawarypayment.entity.User;
import com.fawary.fawarypayment.exception.NotFountException;
import com.fawary.fawarypayment.exception.SecurityAuthenticationException;
import com.fawary.fawarypayment.repo.UsersRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final UsersRepo usersRepo;


    public UserService(AuthenticationManager authenticationManager, Environment env, UsersRepo usersRepo) {
        this.authenticationManager = authenticationManager;
        this.env = env;
        this.usersRepo = usersRepo;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        String jwt="";
        Authentication authentication= UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDto.username()
                ,loginRequestDto.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if(null!=authenticationResponse && authenticationResponse.isAuthenticated()){

            String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                    ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            User user = usersRepo.findByUsername(loginRequestDto.username()).orElseThrow(()-> new NotFountException("User not found"));
            jwt= Jwts.builder().issuer("fawary system").subject("token")
                    .claim("username",authenticationResponse.getName())
                    .claim("billerId",user.getBiller()==null?"":user.getBiller().getId())
                    .claim("Roles",authenticationResponse.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                    .issuedAt(new java.util.Date())
                    .expiration(new java.util.Date((new java.util.Date()).getTime()+30000000))
                    .signWith(secretKey).compact();
            return new LoginResponseDto(jwt);
        }
        throw new SecurityAuthenticationException("Invalid username or password");
    }
}
