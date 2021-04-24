package com.equisoft.dca.backend.exception;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class EntityReferenceConflictedException extends BaseException {

	private static final long serialVersionUID = -2916293585670014306L;

	static final String MESSAGE = "%1$s %2$s cannot be deleted because it is associated to a %3$s";

	private static final String messageCode = "entity.referenceconflicted";

	public EntityReferenceConflictedException(Class clazz, Class clazzReference, String... args) {
		super(generateMessage(clazz.getSimpleName(), clazzReference.getSimpleName(), ExceptionUtil.toMap(String.class, String.class, args)),
				ArrayUtils.addFirst(ArrayUtils.add(args, clazzReference.getSimpleName().toLowerCase()), clazz.getSimpleName().toLowerCase()));
	}

	private static String generateMessage(String entity, String entityReference, Map<String, String> searchParams) {
		return String.format(MESSAGE, StringUtils.capitalize(entity), searchParams, StringUtils.capitalize(entityReference));
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
