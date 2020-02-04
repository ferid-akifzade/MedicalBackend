package org.medical.api.service;

import org.medical.libs.OrdinaryUser;
import org.medical.libs.req.LoginRequest;
import org.medical.repositories.OrdinaryUserRepo;
import org.medical.security.jwt.Const;
import org.medical.security.jwt.JwtAuthService;
import org.medical.security.seclib.OrdinaryUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtAuthService authService;
    private final OrdinaryUserRepo userRepo;
    private String token;
    public LoginService(AuthenticationManager authenticationManager, JwtAuthService authService, OrdinaryUserRepo userRepo) {
        token = "";
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userRepo = userRepo;
    }
    public Optional<OrdinaryUser> getOrdinaryUserById(Long id){
        return userRepo.findById(id);
    }

    public Optional<String> login(LoginRequest response){
        return Optional.of(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(response.getEmail(),response.getPassword())))
                .filter(Authentication::isAuthenticated)
                .map(authentication -> {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return authentication;
                })
                .map(authentication -> (OrdinaryUserDetails) authentication)
                .map(ordinaryUserDetails -> authService.generateToken(ordinaryUserDetails.getId(),!response.getRemember().isEmpty()))
                .map(token -> String.format("%s%s", Const.AUTH_BEARER,token));
    }
    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }
    public void resetToken(){
        this.token = "";
    }
    public Optional<OrdinaryUser> getOrdinaryUserFromToken(String token){
        return Optional.of(token)
                .flatMap(authService::tokenToClaims)
                .map(authService::extractIdFromClaims)
                .flatMap(this::getOrdinaryUserById);
    }

}
