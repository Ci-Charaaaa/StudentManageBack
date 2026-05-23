package com.java.studentmanage;

import com.java.studentmanage.config.DatabaseInitializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.java.studentmanage.mapper")  //用MyBatis-Plus扫描Mapper接口包
@SpringBootApplication                        //把各种依赖配置好，扫描包里所有的组件，声明该类是配置类
public class StudentManageApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StudentManageApplication.class);
        app.addInitializers(new DatabaseInitializer()); //注册数据库初始化器
        app.run(args);                                  //启动内嵌Tomcat
    }

}
