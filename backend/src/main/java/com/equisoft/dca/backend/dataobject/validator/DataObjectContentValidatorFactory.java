package com.equisoft.dca.backend.dataobject.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.equisoft.dca.backend.dataobject.model.DataObjectType;

@Component
public class DataObjectContentValidatorFactory {

	private static final Map<DataObjectType, Supplier<DataObjectContentValidator>> map = new HashMap<>();

	static {
		map.put(DataObjectType.COBOL_COPYBOOK, CobolDataObjectContentValidator::new);
	}

	public DataObjectContentValidator createDataObjectContentValidator(DataObjectType dataObjectType) {
		return Optional.ofNullable(map.get(dataObjectType))
				.map(Supplier::get)
				.orElseThrow(() -> new IllegalArgumentException(
						"DataObjectType {" + (dataObjectType != null ? dataObjectType.toString() : "null") + "} not mapped"));
	}
}
