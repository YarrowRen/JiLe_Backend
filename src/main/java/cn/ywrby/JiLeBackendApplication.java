package cn.ywrby;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.ywrby.mapper")
public class JiLeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiLeBackendApplication.class, args);
    }

}
