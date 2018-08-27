package cn.tanlw.db.lastInsertId;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @create 2018/8/27
 */

@PropertySource({"classpath:application.properties","classpath:application-loc.properties"})
@SpringBootApplication(scanBasePackages = "cn.tanlw.db")
public class TestApplication {
    public static void main(String[] args) {
        new SpringApplication(TestApplication.class).run(args);
    }
}
