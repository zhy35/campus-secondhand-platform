package com.campus.secondhand.controller;

import com.campus.secondhand.dto.ApiResponse;
import com.campus.secondhand.dto.LoginRequest;
import com.campus.secondhand.dto.LoginResponse;
import com.campus.secondhand.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    // 用户登录
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request.getUsername(), request.getPassword());

        if (loginResponse.isSuccess()) {
            return ApiResponse.success("登录成功", loginResponse);
        } else {
            return ApiResponse.error(loginResponse.getMessage());
        }
    }

    // 验证令牌
    @GetMapping("/verify")
    public ApiResponse<Boolean> verifyToken(@RequestHeader("Authorization") String token) {
        // 移除 "Bearer " 前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean isValid = authService.verifyToken(token);
        return ApiResponse.success(isValid ? "令牌有效" : "令牌无效", isValid);
    }
}