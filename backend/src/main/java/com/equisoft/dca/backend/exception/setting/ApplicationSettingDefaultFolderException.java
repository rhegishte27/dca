package com.equisoft.dca.backend.exception.setting;

import com.equisoft.dca.backend.exception.BaseException;

public class ApplicationSettingDefaultFolderException extends BaseException {
	private static final long serialVersionUID = 4194627067854866520L;

	private static final String MESSAGE = "%1$s must be valid and must be sub folder of Common Folder";

	private static final String messageCode = "setting.defaultfolder.valid";

	public ApplicationSettingDefaultFolderException(String folderName, String folderNameCode) {
		super(generateMessage(folderName), folderNameCode);
	}

	private static String generateMessage(String folderName) {
		return String.format(MESSAGE, folderName);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
