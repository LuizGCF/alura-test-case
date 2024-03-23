package com.alura.coursecase.services;

import com.alura.coursecase.controllers.response.GetUserResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.UserAlreadyExistsException;
import com.alura.coursecase.exception.UserNotFoundException;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createUser(UserModel userModel) {
        if (userRepository.existsByUsername(userModel.getUsername()))
            throw new UserAlreadyExistsException(ErrorsEnum.A102.code, String.format(ErrorsEnum.A102.message, "username"));

        if (userRepository.existsByEmail(userModel.getEmail()))
            throw new UserAlreadyExistsException(ErrorsEnum.A102.code, String.format(ErrorsEnum.A102.message, "email"));

        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(userModel);
    }

    public GetUserResponse findUserByUsername(String username) {
        Optional<UserModel> user = userRepository.findByUsername(username);

        return user.map(u -> GetUserResponse.fromModel(u)).orElseThrow(() -> new UserNotFoundException(ErrorsEnum.A101.code, ErrorsEnum.A101.message));
    }

}
