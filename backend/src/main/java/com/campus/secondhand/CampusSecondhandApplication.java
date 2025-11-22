package com.campus.secondhand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 移除 @RestController 注解
public class CampusSecondhandApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusSecondhandApplication.class, args);
        printStartupInfo();
    }

    // 移除所有测试端点，让专门的控制器处理
    // @GetMapping("/test-root") - 移除
    // @GetMapping("/api/direct-test") - 移除
    // @GetMapping("/api/ping") - 移除

    private static void printStartupInfo() {
        System.out.println("\n" +
                "=========================================\n" +
                "🎉 校园二手交易平台后端服务启动成功！\n" +
                "📍 服务地址: http://localhost:8088\n" +
                "📚 API 基础路径: /api\n" +
                "🗄️  数据库: campus_secondhand\n" +
                "=========================================\n"
        );
    }
}