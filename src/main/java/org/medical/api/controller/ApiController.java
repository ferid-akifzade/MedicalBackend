package org.medical.api.controller;

import org.medical.libs.resp.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/")
    public String reqHome(){
        return "Ok: Home";
    }

    @GetMapping("/login")
    public ApiResponse getLogin() {
        return new ApiResponse("OK: Login");
    }

    @GetMapping("/register")
    public ApiResponse getRegister() {
        return new ApiResponse("OK: Register");
    }
    @GetMapping("/exam")
    public ApiResponse getExam() {
        return new ApiResponse("OK: Exam");
    }
}
