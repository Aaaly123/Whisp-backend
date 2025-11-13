package com.example.demo.services;


import com.example.demo.dtos.*;

import java.util.List;

public interface UserService {


    UserResponseDTO getUserByEmail(String email);


    public List<UserResponseDTO> getAllUsers();

    public UserResponseDTO createUser(UserSignUpRequestDTO userSignUpRequestDTO);

    public SignInResponseDTO login(UserSignInRequestDTO userSignInRequestDTO);


    public String updateUserBio(String bio, String email);

    String updateUserPassword(UpdatePasswordRequestDTO request, String email);

    ReceiverProfileResponseDTO getReceiverProfileById(Long receiverId);

    List<ReceiverProfileResponseDTO> getReceiverProfileByName(String name);

}


