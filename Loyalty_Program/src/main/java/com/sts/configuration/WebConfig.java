package com.sts.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**")
         .allowedOrigins("https://loyalty-project-tau.vercel.app/")
         .allowedMethods("*")
         .allowedHeaders("*");
	}
}
