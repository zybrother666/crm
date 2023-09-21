package com.zy.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.zy.crm.dao")

public class Starter extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }

    /*设置web项目的启动入口*/

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Starter.class);
    }
}
