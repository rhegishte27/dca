package com.equisoft.dca.backend.process.model;

import lombok.Getter;

@Getter
public enum ProcessResult {

	DATA_DICTIONARY("COPYDD.DAT"),
	RESULT("COPY.LST"),
	FLAG("COPYPRC.FLG");

	private final String filename;

	ProcessResult(String filename) {
		this.filename = filename;
	}
}
