package com.equisoft.dca.backend.process.model;

import lombok.Getter;

@Getter
public enum Process {

	IMPORT_COBOL_COPYBOOK("copyprc.exe");

	private final String filename;

	Process(String filename) {
		this.filename = filename;
	}
}
