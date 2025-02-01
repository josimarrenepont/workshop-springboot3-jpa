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

    public ResponseEntity signing(AccountCredentialsVO data){
        var username = data.getUsername();
        var password = data.getPassword();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch(Exception e){
            throw new BadCredentialsException("Invalid usernama/password suppliers");
        }
        var user = userRepository.findByUsername(username);

        var tokenResponse = new TokenVO();

        if(user != null){
            tokenResponse = tokenProvider.createAccessToken(username, user.get().getRoles());
        } else{
            throw new UsernameNotFoundException("username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken){
        var user = userRepository.findByUsername(username);

        var tokenResponse = new TokenVO();
        if(user != null){
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else{
            throw new UsernameNotFoundException("username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }
}
