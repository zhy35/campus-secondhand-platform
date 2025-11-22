package com.campus.secondhand.controller;

import com.campus.secondhand.dto.ApiResponse;
import com.campus.secondhand.dto.RegisterRequest;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    // 测试接口
    @GetMapping("/test")
    public String test() {
        return "用户服务测试成功！";
    }

    // 用户注册
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ApiResponse.success("注册成功", user);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // 检查用户名是否存在
    @GetMapping("/check-username")
    public ApiResponse<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        String message = exists ? "用户名已存在" : "用户名可用";
        return ApiResponse.success(message, exists);
    }

    // 检查邮箱是否存在
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        String message = exists ? "邮箱已被注册" : "邮箱可用";
        return ApiResponse.success(message, exists);
    }

    // 根据ID获取用户信息
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return ApiResponse.success("获取用户信息成功", user);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}