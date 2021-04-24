package com.equisoft.dca.backend.ftp.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class FtpConnectionFailureException extends BaseException {

	private static final long serialVersionUID = 265608131867572385L;

	private static final String MESSAGE = "FTP server connection failed: {%1$s}";

	private static final String messageCode = "ftp.connectionfailure.exception";

	public FtpConnectionFailureException(String error) {
		super(String.format(MESSAGE, error));
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
