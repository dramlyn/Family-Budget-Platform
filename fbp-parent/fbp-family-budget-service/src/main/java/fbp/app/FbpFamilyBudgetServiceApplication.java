package fbp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"fbp.app.repository", "fbp.app.model", "fbp.app.security"})
public class FbpFamilyBudgetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FbpFamilyBudgetServiceApplication.class, args);
    }
}
