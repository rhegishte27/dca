package com.equisoft.dca.backend.process.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.process.exception.ProcessNotFoundException;
import com.equisoft.dca.backend.process.model.Process;

@Service
class ProcessServiceImpl implements ProcessService {

	@Value("${dca.programs.directory}")
	private String dcaProgramsDirectory;

	private static final long processTimeout = 30;

	private final FileSystemService fileSystemService;

	@Inject
	ProcessServiceImpl(FileSystemService fileSystemService) {
		this.fileSystemService = fileSystemService;
	}

	@Override
	public boolean execute(Process process, String... arguments) throws IOException, InterruptedException {
		return new ProcessBuilder(addProcessToArguments(process, arguments))
				.start()
				.waitFor(processTimeout, TimeUnit.SECONDS);
	}

	private List<String> addProcessToArguments(Process process, String... arguments) {
		List<String> args = new ArrayList<>(Arrays.asList(arguments));
		args.add(0, getProcessFilePath(process));
		return args;
	}

	private String getProcessFilePath(Process process) {
		return Optional.ofNullable(getResource(dcaProgramsDirectory + process.getFilename()))
				.map(fileSystemService::getPathFromUrl)
				.orElseThrow(() -> new ProcessNotFoundException(process.toString()));
	}

	private URL getResource(String resourceName) {
		return Thread.currentThread().getContextClassLoader().getResource(resourceName);
	}
}
