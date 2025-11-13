package com.example.demo.services;



import com.example.demo.dtos.*;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    public UserResponseDTO convertToEntity;


    public User UserSignUpRequestDTOToUser(UserSignUpRequestDTO userSignUpRequestDTO){
        User user = new User();
        user.setName(userSignUpRequestDTO.getName());
        user.setEmail(userSignUpRequestDTO.getEmail());
        user.setBio(userSignUpRequestDTO.getBio());
        user.setPassword(userSignUpRequestDTO.getPassword());
        return user;
    }


    public UserResponseDTO userToUserResponseDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setBio(user.getBio());
        return userResponseDTO;
    }

    public UserResponseDTO createUser(UserSignUpRequestDTO userSignUpRequestDTO){
        User user = UserSignUpRequestDTOToUser(userSignUpRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userToUserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::userToUserResponseDTO).collect(Collectors.toList());
    }

    public SignInResponseDTO login(UserSignInRequestDTO userSignInRequestDTO) {
        String email = userSignInRequestDTO.getEmail();
        String rawPassword = userSignInRequestDTO.getPassword();

        // find user
        Optional<User> optionalUser = userRepository.findByEmail(email);
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO();

        // check if user exists
        if (optionalUser.isEmpty()) {
            signInResponseDTO.setMessage("Invalid Email");
            return signInResponseDTO;
        }

        User user = optionalUser.get(); //  safely get user

        // check password
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            signInResponseDTO.setMessage("Login Successful!");
            signInResponseDTO.setToken(token);
        } else {
            signInResponseDTO.setMessage("Invalid Password");
        }

        return signInResponseDTO;
    }


    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getBio());
    }


    public String updateUserBio(String bio, String email){
        User user = userRepository.findByEmail(email).orElseThrow();

        user.setBio(bio);

        userRepository.save(user);

        return "Bio Updated Successfully.";
    }

    @Override
    public String updateUserPassword(UpdatePasswordRequestDTO request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if old password matches
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return "Old password is incorrect!";
        }

        // Encode and update new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password updated successfully.";
    }

    public ReceiverProfileResponseDTO getReceiverProfileById(Long receiverId){
        User user = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ReceiverProfileResponseDTO(user.getId(), user.getName(), user.getBio());
    }



    public List<ReceiverProfileResponseDTO> getReceiverProfileByName(String name){

        List<User> users = userRepository.findAllByNameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            throw new RuntimeException("No users found with this name");
        }

        return users.stream()
                .map(user -> new ReceiverProfileResponseDTO(user.getId(), user.getName(), user.getBio()))
                .collect(Collectors.toList());

    }

}

