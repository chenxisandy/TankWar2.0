package com.example.tankwar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TankBattleApplication...
 */
@SpringBootApplication
public class TankBattleApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(TankBattleApplication.class, args);
        System.out.println("started...");
    }
}
