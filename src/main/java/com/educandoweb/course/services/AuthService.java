package com.educandoweb.course.services;

import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.security.AccountCredentialsVO;
import com.educandoweb.course.security.TokenVO;
import com.educandoweb.course.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenVO> signin(AccountCredentialsVO data) {

        var username = data.getUsername();
        var password = data.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password");
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Username " + username + " not found!")
                );

        var token = tokenProvider.createAccessToken(
                username,
                user.getRoles()
        );

        return ResponseEntity.ok(token);
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity<TokenVO> refreshToken(String username, String refreshToken) {

        userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Username " + username + " not found!")
                );

        var token = tokenProvider.refreshToken(refreshToken);

        return ResponseEntity.ok(token);
    }
}
