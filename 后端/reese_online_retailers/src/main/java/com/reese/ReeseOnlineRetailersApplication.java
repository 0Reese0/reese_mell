package com.reese;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@MapperScan("com.reese.mapper")
@SpringBootApplication
public class ReeseOnlineRetailersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReeseOnlineRetailersApplication.class, args);
    }

}
