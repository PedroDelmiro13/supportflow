package com.supportflow.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.supportflow.auth.dto.LoginRequest;
import com.supportflow.auth.dto.LoginResponse;
import com.supportflow.auth.security.jwt.JwtService;
import com.supportflow.entity.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        Usuario userDetails = (Usuario) authentication.getPrincipal();
        String token = this.jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}
