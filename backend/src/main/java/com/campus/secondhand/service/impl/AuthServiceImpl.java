package com.campus.secondhand.service.impl;

import com.campus.secondhand.dto.LoginResponse;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.AuthService;
import com.campus.secondhand.service.UserService;
import com.campus.secondhand.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(String username, String password) {
        try {
            // 验证用户凭证
            User user = userService.login(username, password);

            // 生成JWT令牌
            String token = jwtUtil.generateToken(user);

            // 返回登录成功响应
            return LoginResponse.success(token, user);

        } catch (RuntimeException e) {
            return LoginResponse.error(e.getMessage());
        }
    }

    @Override
    public boolean verifyToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public User getUserFromToken(String token) {
        if (!verifyToken(token)) {
            return null;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        return userService.findByUsername(username);
    }
}