package com.equisoft.dca.backend.location.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.equisoft.dca.backend.exception.BaseException;

public class LocationReferenceConflictedException extends BaseException {

	private static final long serialVersionUID = -2916293585670014306L;

	static final String MESSAGE = "Location {Name = %1$s} cannot be deleted because it is associated to a Project - projects = %2$s";

	private static final String messageCode = "locationreference.exception";

	public LocationReferenceConflictedException(String locationName, List<String> projects) {
		super(generateMessage(locationName,projects), locationName, projects.stream().collect(Collectors.joining(",")));
	}

	private static String generateMessage(String locationName, List<String> projects) {
		return String.format(MESSAGE, locationName, projects.stream().collect(Collectors.joining(",")));
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}
}
