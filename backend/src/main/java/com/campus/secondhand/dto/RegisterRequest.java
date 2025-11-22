package com.campus.secondhand.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotBlank(message = "学号不能为空")
    private String studentId;

    // 构造方法
    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email, String phone, String realName, String studentId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.realName = realName;
        this.studentId = studentId;
    }

    // Getter 和 Setter 方法
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}