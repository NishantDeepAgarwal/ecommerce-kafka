package com.order.service.service;

import com.order.service.dto.RegisterUserRequest;
import com.order.service.entity.UserResponse;
import com.order.service.entity.Users;
import com.order.service.repository.UserDetailsRepository;
import com.order.service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserResponse registerUser(RegisterUserRequest registerUserRequest){
        if(userDetailsRepository.findByUsername(registerUserRequest.getUsername()).isPresent()){
            throw new RuntimeException("User Already Exist");
        }
        Users users = new Users();
        users.setUsername(registerUserRequest.getUsername());
        users.setRole(registerUserRequest.getRole());
        users.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        Users savedUser = userDetailsRepository.save(users);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(),savedUser.getRole().name());
    }

    public String generateTokenFromRequest(RegisterUserRequest registerUserRequest){
        Authentication authentication=new UsernamePasswordAuthenticationToken(
                registerUserRequest.getUsername(), registerUserRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }
}
