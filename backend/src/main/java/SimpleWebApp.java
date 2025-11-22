import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SimpleWebApp {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleWebApp.class, args);
        System.out.println("✅ SimpleWebApp 启动成功！");
        System.out.println("🌐 访问: http://localhost:8080/ping");
    }
}