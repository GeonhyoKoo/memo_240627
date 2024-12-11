package com.memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class MemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoApplication.class, args);
	}

}
