package org.cent.springBootDemo.entrance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Boot启动入口
 * Created by cent on 2016/9/2.
 */
@EnableWebMvc
@Configuration
@SpringBootApplication(scanBasePackages = {"org.cent.springBootDemo"})
public class Application extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
