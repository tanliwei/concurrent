package cn.tanlw.db.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @create 2018/8/27
 */

@PropertySource({"classpath:db/application.properties","classpath:db/application-loc.properties"})
@SpringBootApplication(scanBasePackages = "cn.tanlw.db.lock")
public class TestApplication {
    public static void main(String[] args) {
        new SpringApplication(TestApplication.class).run(args);
    }
}
