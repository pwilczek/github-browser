package name.wilu.a.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrix
@SpringBootApplication
@EnableHystrixDashboard
public class BrowserApp {

    public static void main(String[] args) {
        SpringApplication.run(BrowserApp.class, args);
    }
}
