package com.campus.secondhand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CampusSecondhandApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusSecondhandApplication.class, args);
        printStartupInfo();
    }

    // 测试不带 /api 路径的端点（直接根路径）
    @GetMapping("/test-root")
    public String testRoot() {
        return "Root Test OK! - 不带API路径";
    }

    // 测试带 /api 路径的端点
    @GetMapping("/api/direct-test")
    public String directTest() {
        return "Direct Test OK - 主应用类测试成功！";
    }

    // 另一个测试端点
    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }

    private static void printStartupInfo() {
        System.out.println("\n" +
                "=========================================\n" +
                "🎉 校园二手交易平台后端服务启动成功！\n" +
                "📍 服务地址: http://localhost:8088\n" +  // 修正端口
                "📚 API 基础路径: /api\n" +
                "🗄️  数据库: campus_secondhand\n" +
                "=========================================\n"
        );
    }
}