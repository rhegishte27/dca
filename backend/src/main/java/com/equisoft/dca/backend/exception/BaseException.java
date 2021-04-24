package com.equisoft.dca.backend.exception;

import org.apache.commons.lang3.SerializationUtils;

public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 7222438967026213574L;

	private String[] args;

	protected BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	protected BaseException(String message, String... args) {
		super(message);
		this.args = args;
	}

	public String[] getMessageArguments() {
		return SerializationUtils.clone(args);
	}

	public abstract String getMessageCode();
}
