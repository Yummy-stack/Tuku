package com.tuku;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tuku.mapper")
public class TuKuApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuKuApplication.class, args);
        System.out.println("\n==================== 项目启动 ====================\n");
    }

}
