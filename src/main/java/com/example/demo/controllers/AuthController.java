package com.example.demo.controllers;


import com.example.demo.dtos.*;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public UserResponseDTO signUp(@RequestBody UserSignUpRequestDTO userSignUpRequestDTO){
        return userService.createUser(userSignUpRequestDTO);
    }

    @PostMapping("/signin")
    public SignInResponseDTO signIn(@RequestBody UserSignInRequestDTO userSignInRequestDTO){
        return userService.login(userSignInRequestDTO);
    }

}
