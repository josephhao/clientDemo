import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"service", "controller"})
@EnableDubbo
@SpringBootApplication
public class DubboClient1Application {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DubboClient1Application.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
