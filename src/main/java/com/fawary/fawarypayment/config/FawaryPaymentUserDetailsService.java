package com.fawary.fawarypayment.config;

import com.fawary.fawarypayment.entity.Users;
import com.fawary.fawarypayment.exception.NotFountEx;
import com.fawary.fawarypayment.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FawaryPaymentUserDetailsService  implements UserDetailsService {
    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepo.findByUsername(username).orElseThrow(()->new NotFountEx("the "+username+" not found"));
        String email = user.getUsername();
        String password = user.getPassword();
        List<GrantedAuthority> roles = user.getRoles().stream().
                map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());

        return new User(email,password,roles);
    }

}
