package com.equisoft.dca.backend.process.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class ProcessNotFoundException extends BaseException {

	private static final long serialVersionUID = -7803571875329757989L;

	private static final String MESSAGE = "The process {%1$s} has not been found";

	private static final String messageCode = "process.notfound.exception";

	public ProcessNotFoundException(String process) {
		super(String.format(MESSAGE, process));
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
