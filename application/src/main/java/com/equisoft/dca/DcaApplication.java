package com.equisoft.dca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {RestApplication.class, BaseApplication.class})
public class DcaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DcaApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DcaApplication.class);
	}
}
