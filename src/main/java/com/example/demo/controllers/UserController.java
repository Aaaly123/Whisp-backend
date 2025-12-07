package com.example.demo.controllers;


import com.example.demo.dtos.ReceiverProfileResponseDTO;
import com.example.demo.dtos.UpdatePasswordRequestDTO;
import com.example.demo.dtos.UserResponseDTO;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public List<UserResponseDTO> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(Authentication authentication) {
        String email = authentication.getName(); // email extracted from JWT
        return userService.getUserByEmail(email);
    }

    @PutMapping("/update-bio")
    public String updateBio(@RequestBody Map<String, String> request, Authentication authentication){
        String email = authentication.getName();
        String bio = request.get("bio");
        return userService.updateUserBio(bio, email);

    }

    @PutMapping("/update-password")
    public String updatePassword(
            @Valid @RequestBody UpdatePasswordRequestDTO request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return userService.updateUserPassword(request, email);
    }



    @GetMapping("/receiver/{id}")
    public ReceiverProfileResponseDTO getReceiverProfileById(@PathVariable Long id) {
        return userService.getReceiverProfileById(id);
    }

    @GetMapping("/receiver/name/{name}")
    public List<ReceiverProfileResponseDTO> getReceiversProfileByName(@PathVariable String name) {
        return userService.getReceiverProfileByName(name);
    }

}
