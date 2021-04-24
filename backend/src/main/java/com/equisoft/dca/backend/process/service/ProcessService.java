package com.equisoft.dca.backend.process.service;

import java.io.IOException;

import com.equisoft.dca.backend.process.model.Process;

public interface ProcessService {

	boolean execute(Process process, String... arguments) throws IOException, InterruptedException;
}
