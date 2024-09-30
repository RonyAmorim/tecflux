package com.tecflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class TecfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(TecfluxApplication.class, args);
	}

}
