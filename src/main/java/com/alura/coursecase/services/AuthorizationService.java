package com.alura.coursecase.services;

import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.UserNotFoundException;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);

        if(!user.isPresent())
            throw new UserNotFoundException(ErrorsEnum.A101.code, ErrorsEnum.A101.message);

        return user.get();
    }
}
