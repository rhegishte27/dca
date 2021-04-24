package com.equisoft.dca.backend.exception.setting;

import com.equisoft.dca.backend.exception.BaseException;

public class ApplicationSettingCommonFolderException extends BaseException {
	private static final long serialVersionUID = -8440102674391492468L;

	private static final String MESSAGE = "Common folder is invalid";

	private static final String messageCode = "setting.commonfolder.valid";

	public ApplicationSettingCommonFolderException() {
		super(MESSAGE);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
