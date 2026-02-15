package com.fawary.fawarypayment.service.security;

import com.fawary.fawarypayment.entity.User;
import com.fawary.fawarypayment.exception.NotFountException;
import com.fawary.fawarypayment.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        User user = usersRepo.findByUsername(username).orElseThrow(()->new NotFountException("the "+username+" not found"));
        String email = user.getUsername();
        String password = user.getPassword();
        List<GrantedAuthority> roles = user.getRoles().stream().
                map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(email,password,roles);
    }



}
