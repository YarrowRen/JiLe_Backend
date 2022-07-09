package cn.ywrby;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.ywrby.mapper")
public class BookStoreDemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreDemo2Application.class, args);
    }

}
