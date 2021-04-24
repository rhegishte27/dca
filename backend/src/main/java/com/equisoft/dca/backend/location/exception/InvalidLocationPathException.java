package com.equisoft.dca.backend.location.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class InvalidLocationPathException  extends BaseException {

	private static final long serialVersionUID = 779823139223719026L;

	private static final String MESSAGE = "The location path is not valid";

	private static final String messageCode = "invalidlocationpath.exception";

	public InvalidLocationPathException() {
		super(MESSAGE);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
