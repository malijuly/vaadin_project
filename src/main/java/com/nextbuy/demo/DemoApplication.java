package com.nextbuy.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		printUrls();
	}


	private static void printUrls() {
		StringBuilder sb = new StringBuilder();
		sb.append("**************************************************************\n");
		sb.append("             http://localhost:8080\n");
		sb.append("             http://localhost:8080/api/weather/\n");
		sb.append("             http://localhost:8080/h2-console\n");
		sb.append("**************************************************************");
		System.out.println(sb.toString());
	}

}
