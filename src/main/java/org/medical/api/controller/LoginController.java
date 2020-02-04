package org.medical.api.controller;

import org.medical.api.service.LoginService;
import org.medical.libs.OrdinaryUser;
import org.medical.libs.req.LoginRequest;
import org.medical.libs.resp.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService service;
    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponse postLogin(@RequestBody LoginRequest response) {
       return service.login(response)
               .map(s -> {service.setToken(s); return s;})
               .flatMap(service::getOrdinaryUserFromToken)
               .map(user -> new LoginResponse(user,service.getToken()))
               .map(login -> {service.resetToken(); return login;})
               .orElse(new LoginResponse(new OrdinaryUser(),""));
    }
}
