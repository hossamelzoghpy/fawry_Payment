package com.fawry.fawrypayment.controller;

import com.fawry.fawrypayment.constants.ApplicationConstants;

import com.fawry.fawrypayment.dto.LoginRequestDto;
import com.fawry.fawrypayment.dto.LoginResponseDto;
import com.fawry.fawrypayment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<LoginResponseDto> loginApi(@RequestBody @Validated LoginRequestDto loginRequestDto){

        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,loginResponseDto.jwtToken())
                .body(loginResponseDto);
    }
}
