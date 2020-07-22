package com.eoullim;

import com.eoullim.domain.Member;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.swing.*;

@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);


        /*
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        String[] beanList = ctx.getBeanDefinitionNames();
        for(String s : beanList){
            System.out.println("=====MY bean : "+s+" =======");
        }
        */
    }
}