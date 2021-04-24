package com.equisoft.dca.backend;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendConfiguration {

	@Bean
	public FileSystem getFileSystem() {
		return FileSystems.getDefault();
	}
}
