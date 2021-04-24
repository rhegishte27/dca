package com.equisoft.dca.backend.user.exception;

import com.equisoft.dca.backend.exception.BaseException;

public class UserRoleLevelException extends BaseException {

	private static final long serialVersionUID = 5959253779605215028L;

	private static final String MESSAGE = "User {%1$s:%2$s} is not allowed to modify User {%3$s:%4$s}";

	private static final String messageCode = "userrolelevel.exception";

	public UserRoleLevelException(String... args) {
		super(String.format(MESSAGE, args), args);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
