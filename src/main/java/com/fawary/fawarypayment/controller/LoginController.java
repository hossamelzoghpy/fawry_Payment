package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.constants.ApplicationConstants;
import com.fawary.fawarypayment.dto.LoginRequestDto;
import com.fawary.fawarypayment.dto.LoginResponseDto;
import com.fawary.fawarypayment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginApi(@RequestBody LoginRequestDto loginRequestDto){

        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,loginResponseDto.jwtToken())
                .body(loginResponseDto);
    }
}
