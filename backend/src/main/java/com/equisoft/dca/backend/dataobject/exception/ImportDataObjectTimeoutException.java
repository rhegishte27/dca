package com.equisoft.dca.backend.dataobject.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class ImportDataObjectTimeoutException extends BaseException {

	private static final long serialVersionUID = -3655433103180649881L;

	private static final String MESSAGE = "The process to import data object has not finished";

	private static final String messageCode = "importdataobject.timeout.exception";

	public ImportDataObjectTimeoutException() {
		super(MESSAGE);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
