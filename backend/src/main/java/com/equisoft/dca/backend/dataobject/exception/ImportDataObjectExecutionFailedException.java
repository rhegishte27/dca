package com.equisoft.dca.backend.dataobject.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class ImportDataObjectExecutionFailedException extends BaseException {

	private static final long serialVersionUID = -2793029293617874379L;

	private static final String MESSAGE = "The process to import the data object failed";

	private static final String messageCode = "importdataobject.executionfailed.exception";

	public ImportDataObjectExecutionFailedException(Throwable cause) {
		super(MESSAGE, cause);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
