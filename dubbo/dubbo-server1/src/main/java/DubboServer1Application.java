import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"service"})
@EnableDubbo(scanBasePackages = {"service"})
@SpringBootApplication
public class DubboServer1Application {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DubboServer1Application.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
