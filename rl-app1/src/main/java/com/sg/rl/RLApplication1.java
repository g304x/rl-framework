package com.sg.rl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class RLApplication1 {
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        System.out.println("123");
        SpringApplication.run(RLApplication1.class, args);

    }
}
