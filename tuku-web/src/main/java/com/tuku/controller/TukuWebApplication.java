package com.tuku.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@ComponentScan(basePackages = {"com.tuku","com.qcloud"})
@MapperScan("com.tuku.tukuMapper")
public class TukuWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TukuWebApplication.class, args);
        System.out.println("\n ----------------------------- Tuku项目启动成功 ----------------------------- \n");
    }

}
