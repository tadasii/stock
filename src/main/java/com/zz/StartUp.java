package com.zz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by zhangzheng on 2019/10/15.
 */
@SpringBootApplication
public class StartUp extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(StartUp.class, args);
    }
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        builder.sources(StartUp.class);
        return builder;
    }
}
