package com.equisoft.dca.backend.dataobject.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class InvalidDataObjectContentException extends BaseException {

	private static final long serialVersionUID = -742873183542728078L;

	private static final String MESSAGE = "The content of the data object is not valid";

	private static final String messageCode = "invaliddataobjectcontent.exception";

	public InvalidDataObjectContentException() {
		super(MESSAGE);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
