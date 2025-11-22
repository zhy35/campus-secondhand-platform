package com.campus.secondhand.service;

import com.campus.secondhand.entity.User;
import com.campus.secondhand.dto.RegisterRequest;

public interface UserService {

    // 用户注册
    User register(RegisterRequest request);

    // 用户登录
    User login(String username, String password);

    // 根据用户名查找用户
    User findByUsername(String username);

    // 根据ID查找用户
    User findById(Long id);

    // 检查用户名是否存在
    boolean existsByUsername(String username);

    // 检查邮箱是否存在
    boolean existsByEmail(String email);

    // 检查学号是否存在
    boolean existsByStudentId(String studentId);
}