package com.equisoft.dca.backend.exception;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class EntityAlreadyExistsException extends BaseException {

	private static final long serialVersionUID = -3375515485775028847L;

	private static final String MESSAGE = "%1$s %2$s already exists";

	private static final String messageCode = "entity.alreadyexists";

	public EntityAlreadyExistsException(Class clazz, String... args) {
		super(generateMessage(clazz.getSimpleName(), ExceptionUtil.toMap(String.class, String.class, args)),
				ArrayUtils.addFirst(args, clazz.getSimpleName().toLowerCase()));
	}

	private static String generateMessage(String entity, Map<String, String> searchParams) {
		return String.format(MESSAGE, StringUtils.capitalize(entity), searchParams);
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
