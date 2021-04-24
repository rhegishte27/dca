package com.equisoft.dca.api;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Equisoft/DCA API", description = "Automatically generated API documentation", version = "1"),
		servers = {@Server(url = "/{contextPath}/api/v1", variables = @ServerVariable(name = "contextPath", defaultValue = "DCA"))})
public class SwaggerConfig {
}
