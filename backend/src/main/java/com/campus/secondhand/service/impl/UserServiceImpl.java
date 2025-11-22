package com.campus.secondhand.service.impl;

import com.campus.secondhand.entity.User;
import com.campus.secondhand.dto.RegisterRequest;
import com.campus.secondhand.repository.UserRepository;
import com.campus.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(RegisterRequest request) {
        try {
            System.out.println("=== 开始用户注册流程 ===");
            System.out.println("用户名: " + request.getUsername());
            System.out.println("邮箱: " + request.getEmail());
            System.out.println("学号: " + request.getStudentId());

            // 检查用户名是否已存在
            System.out.println("检查用户名是否存在...");
            boolean usernameExists = existsByUsername(request.getUsername());
            System.out.println("用户名检查结果: " + usernameExists);
            if (usernameExists) {
                throw new RuntimeException("用户名已存在");
            }

            // 检查邮箱是否已存在
            System.out.println("检查邮箱是否存在...");
            boolean emailExists = existsByEmail(request.getEmail());
            System.out.println("邮箱检查结果: " + emailExists);
            if (emailExists) {
                throw new RuntimeException("邮箱已被注册");
            }

            // 检查学号是否已存在
            System.out.println("检查学号是否存在...");
            boolean studentIdExists = existsByStudentId(request.getStudentId());
            System.out.println("学号检查结果: " + studentIdExists);
            if (studentIdExists) {
                throw new RuntimeException("学号已被注册");
            }

            // 创建新用户
            System.out.println("创建用户对象...");
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword()); // 暂时不加密
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setRealName(request.getRealName());
            user.setStudentId(request.getStudentId());
            user.setAvatar("/avatars/default-avatar.png");

            System.out.println("保存用户到数据库...");
            User savedUser = userRepository.save(user);
            System.out.println("用户保存成功，ID: " + savedUser.getId());
            System.out.println("=== 用户注册完成 ===");

            return savedUser;
        } catch (RuntimeException e) {
            System.out.println("注册过程中出现业务异常: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("注册过程中出现系统异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("注册失败: " + e.getMessage());
        }
    }

    @Override
    public User login(String username, String password) {
        try {
            System.out.println("=== 开始用户登录流程 ===");
            System.out.println("登录用户名: " + username);

            // 查找用户
            User user = findByUsername(username);
            System.out.println("找到用户: " + user.getUsername());

            // 检查密码（暂时不加密）
            System.out.println("验证密码...");
            if (!user.getPassword().equals(password)) {
                System.out.println("密码验证失败");
                throw new RuntimeException("密码错误");
            }

            // 检查用户状态
            System.out.println("检查用户状态...");
            if (user.getStatus() != 1) {
                System.out.println("用户状态异常: " + user.getStatus());
                throw new RuntimeException("用户已被禁用");
            }

            System.out.println("=== 用户登录成功 ===");
            return user;
        } catch (RuntimeException e) {
            System.out.println("登录过程中出现业务异常: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("登录过程中出现系统异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("登录失败: " + e.getMessage());
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } catch (Exception e) {
            System.out.println("根据用户名查找用户失败: " + e.getMessage());
            throw new RuntimeException("查找用户失败");
        }
    }

    @Override
    public User findById(Long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } catch (Exception e) {
            System.out.println("根据ID查找用户失败: " + e.getMessage());
            throw new RuntimeException("查找用户失败");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            boolean exists = userRepository.findByUsername(username).isPresent();
            System.out.println("检查用户名 '" + username + "' 存在性: " + exists);
            return exists;
        } catch (Exception e) {
            System.out.println("检查用户名存在性时出错: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            boolean exists = userRepository.findByEmail(email).isPresent();
            System.out.println("检查邮箱 '" + email + "' 存在性: " + exists);
            return exists;
        } catch (Exception e) {
            System.out.println("检查邮箱存在性时出错: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByStudentId(String studentId) {
        try {
            // 使用简单的方法检查学号是否存在
            List<User> allUsers = userRepository.findAll();
            boolean exists = allUsers.stream()
                    .anyMatch(user -> studentId.equals(user.getStudentId()));
            System.out.println("检查学号 '" + studentId + "' 存在性: " + exists);
            return exists;
        } catch (Exception e) {
            System.out.println("检查学号存在性时出错: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}