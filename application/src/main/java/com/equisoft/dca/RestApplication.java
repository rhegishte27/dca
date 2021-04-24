package com.equisoft.dca;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ComponentScan({"com.equisoft.dca.api"})
@EnableAutoConfiguration
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class RestApplication {

	@Bean
	public ServletRegistrationBean apiV1() {
		DispatcherServlet dispatcherServlet = new DispatcherServlet();

		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		dispatcherServlet.setApplicationContext(applicationContext);

		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet, "/api/v1/*");
		servletRegistrationBean.setName("api-v1");
		return servletRegistrationBean;
	}
}
