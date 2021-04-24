package com.equisoft.dca;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
@ComponentScan({"com.equisoft.dca.infra", "com.equisoft.dca.backend"})
@EnableAutoConfiguration
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class BaseApplication {

}
