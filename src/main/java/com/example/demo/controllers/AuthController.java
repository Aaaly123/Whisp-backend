package com.example.demo.controllers;


import com.example.demo.dtos.*;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public UserResponseDTO signUp(@RequestBody @Valid UserSignUpRequestDTO userSignUpRequestDTO){
        return userService.createUser(userSignUpRequestDTO);
    }

    @PostMapping("/signin")
    public SignInResponseDTO signIn(@RequestBody @Valid UserSignInRequestDTO userSignInRequestDTO){
        return userService.login(userSignInRequestDTO);
    }

}
