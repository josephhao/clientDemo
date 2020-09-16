package dubbo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"dubbo"})
@SpringBootApplication
public class DubboClient2Application {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DubboClient2Application.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
