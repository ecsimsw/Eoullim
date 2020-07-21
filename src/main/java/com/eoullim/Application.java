package com.eoullim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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