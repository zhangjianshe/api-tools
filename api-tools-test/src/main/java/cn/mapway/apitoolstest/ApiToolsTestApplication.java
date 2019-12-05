package cn.mapway.apitoolstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ApiToolsTestApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApiToolsTestApplication.class, args);
    }

}
