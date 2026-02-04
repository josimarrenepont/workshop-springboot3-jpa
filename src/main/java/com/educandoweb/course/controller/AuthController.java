package com.educandoweb.course.controller;

import com.educandoweb.course.security.AccountCredentialsVO;
import com.educandoweb.course.security.TokenVO;
import com.educandoweb.course.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.micrometer.common.util.StringUtils.isBlank;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenVO> signin(@RequestBody AccountCredentialsVO data) {

        if (isInvalid(data)) {
            return ResponseEntity.badRequest().build();
        }

        return authService.signin(data);
    }

    @PutMapping("/refresh/{username}")
    public ResponseEntity<TokenVO> refreshToken(
            @PathVariable String username,
            @RequestHeader("Authorization") String refreshToken) {

        if (isInvalid(username, refreshToken)) {
            return ResponseEntity.badRequest().build();
        }

        return authService.refreshToken(username, refreshToken);
    }

    private boolean isInvalid(AccountCredentialsVO data) {
        return data == null
                || isBlank(data.getUsername())
                || isBlank(data.getPassword());
    }

    private boolean isInvalid(String username, String refreshToken) {
        return username == null || username.isBlank()
                || refreshToken == null || refreshToken.isBlank();
    }
}
