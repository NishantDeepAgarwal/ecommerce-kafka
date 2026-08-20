package com.order.service.service;

import com.order.service.dto.RegisterUserRequest;
import com.order.service.entity.UserResponse;


public interface UserService {
    UserResponse registerUser(RegisterUserRequest registerUserRequest);

    String generateTokenFromRequest(RegisterUserRequest registerUserRequest);
}
