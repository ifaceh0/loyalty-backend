package com.sts;
	
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement
public class LoyaltyProgramApplication {
	public static void main(String[] args){
		SpringApplication.run(LoyaltyProgramApplication.class, args);
	}
}
