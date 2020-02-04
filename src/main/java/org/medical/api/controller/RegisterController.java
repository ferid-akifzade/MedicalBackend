package org.medical.api.controller;

import org.medical.api.service.RegisterService;
import org.medical.libs.OrdinaryUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public Object postRegister(@RequestBody OrdinaryUser user){
        return registerService.register(user);
    }
}
