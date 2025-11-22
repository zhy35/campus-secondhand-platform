package com.campus.secondhand.service;

import com.campus.secondhand.dto.LoginResponse;
import com.campus.secondhand.entity.User;

public interface AuthService {
    LoginResponse login(String username, String password);
    boolean verifyToken(String token);
    User getUserFromToken(String token);
}