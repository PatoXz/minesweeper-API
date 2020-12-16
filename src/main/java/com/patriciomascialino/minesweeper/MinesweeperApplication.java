package com.patriciomascialino.minesweeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class MinesweeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }

}
