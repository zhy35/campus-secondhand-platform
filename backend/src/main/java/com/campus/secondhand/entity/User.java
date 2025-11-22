package com.campus.secondhand.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(name = "student_id", unique = true, length = 20)
    private String studentId;

    private String avatar;

    @Column(name = "credit_score")
    private Integer creditScore = 100;

    private Integer status = 1;

    @Column(name = "created_time")
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column(name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();

    // 默认构造方法
    public User() {
    }

    // 带参数的构造方法
    public User(String username, String password, String email, String phone, String realName, String studentId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.realName = realName;
        this.studentId = studentId;
    }

    // Getter 方法
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRealName() {
        return realName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    // Setter 方法
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}