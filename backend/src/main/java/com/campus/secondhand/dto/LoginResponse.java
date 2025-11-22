package com.campus.secondhand.dto;

import com.campus.secondhand.entity.User;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private User user;

    // 构造方法
    public LoginResponse() {}

    public LoginResponse(boolean success, String message, String token, User user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    // 成功登录
    public static LoginResponse success(String token, User user) {
        return new LoginResponse(true, "登录成功", token, user);
    }

    // 登录失败
    public static LoginResponse error(String message) {
        return new LoginResponse(false, message, null, null);
    }

    // Getter 和 Setter
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}