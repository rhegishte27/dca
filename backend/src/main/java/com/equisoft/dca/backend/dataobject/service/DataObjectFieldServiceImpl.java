package com.equisoft.dca.backend.dataobject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.dataobject.dao.DataObjectFieldRepository;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.model.DataObjectField;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;
import com.equisoft.dca.backend.dataobject.model.FieldDataType;
import com.equisoft.dca.backend.dataobject.model.FieldDdlType;

@Service
class DataObjectFieldServiceImpl implements DataObjectFieldService {

	private final DataObjectFieldRepository repository;

	@Inject
	DataObjectFieldServiceImpl(DataObjectFieldRepository repository) {
		this.repository = repository;
	}

	public List<DataObjectField> save(DataObject dataObject) {
		List<DataObjectField> dataObjectFields = new ArrayList<>();
		String[] lines = dataObject.getDataObjectFiles().get(0).getResultContent().split(System.lineSeparator());
		int orderNumber = 1;
		boolean insideDataDefinitionFileProcessingBlock = false;

		for (String line : lines) {

			if (isDataDefinitionFileProcessingBlock(line)) {
				insideDataDefinitionFileProcessingBlock = !insideDataDefinitionFileProcessingBlock;
			}

			if (containsCdws(line)) {
				if (notContainsA(line)) {
					dataObjectFields.add(DataObjectField.builder()
							.dataObject(dataObject)
							.orderNumber(orderNumber++)
							.name(getName(line))
							.level(getLevel(line))
							.occurs(getOccurs(line))
							.redefines(getRedefines(line))
							.dataType(getDataType(line))
							.ddlType(getDdlType(line))
							.rawDeclaration(getRawDeclaration(line))
							.build());
				}

			} else if (startsWithFieldName(line)) {
				dataObjectFields.stream()
						.filter(sameName(getFieldName(line)))
						.findFirst()
						.map(f -> {
							f.setOriginalName(getOriginalName(line));
							f.setValue(getValue(line));
							return f;
						});
			} else if (insideDataDefinitionFileProcessingBlock && containsMessage(line) && dataObjectFields.size() > 0) {
				dataObjectFields.get(dataObjectFields.size() - 1).setStatus(getStatus(line));
				dataObjectFields.get(dataObjectFields.size() - 1).setMessage(getMessage(line));
			}
		}
		repository.deleteByDataObjectId(dataObject.getId());
		return repository.saveAll(dataObjectFields);
	}

	@Override
	public void deleteByDataObjectId(Integer dataObjectId) {
		Objects.requireNonNull(dataObjectId, "dataObjectId cannot be null");
		repository.deleteByDataObjectId(dataObjectId);
	}

	private boolean containsCdws(String line) {
		final String cdws = "***CDWS";
		final int cdwsPosition = 200;
		return line.length() > 205 && line.substring(cdwsPosition).startsWith(cdws);
	}

	private boolean notContainsA(String line) {
		final String a = "A";
		final int aPosition = 217;
		return line.length() > 218 && !line.substring(aPosition).startsWith(a);
	}

	private boolean startsWithFieldName(String line) {
		final String fieldName = "FieldName=";
		return line.startsWith(fieldName);
	}

	private String getName(String line) {
		final int namePosition = 219;
		final int nameSize = 32;
		return line.substring(namePosition, namePosition + nameSize).trim();
	}

	private int getLevel(String line) {
		final int levelPosition = 60;
		final int levelSize = 2;
		return NumberUtils.isCreatable(line.substring(levelPosition, levelPosition + levelSize))
				? Integer.parseInt(line.substring(levelPosition, levelPosition + levelSize))
				: 1;
	}

	private String getOccurs(String line) {
		final String occurs = "OCCURS=";
		int position = line.indexOf(occurs);
		if (position == -1) {
			return null;
		}
		return line.substring(position + occurs.length(), line.indexOf(" ", position + occurs.length()));
	}

	private String getRedefines(String line) {
		final int redefinesPosition = 587;
		final int redefinesSize = 32;
		return line.substring(redefinesPosition, redefinesPosition + redefinesSize).trim();
	}

	private FieldDataType getDataType(String line) {
		final int dataTypePosition = 252;
		final int dataTypeSize = 18;
		return FieldDataType.from(line.substring(dataTypePosition, dataTypePosition + dataTypeSize).trim());
	}

	private FieldDdlType getDdlType(String line) {
		final int ddlTypePosition = 526;
		final int ddlTypeSize = 60;
		return FieldDdlType.from(line.substring(ddlTypePosition, ddlTypePosition + ddlTypeSize).trim());
	}

	private String getRawDeclaration(String line) {
		final int rawDeclarationPosition = 270;
		final int rawDeclarationSize = 256;
		return line.substring(rawDeclarationPosition, rawDeclarationPosition + rawDeclarationSize).trim();
	}

	private Predicate<DataObjectField> sameName(String fieldname) {
		return f -> f.getName().equals(fieldname);
	}

	private String getFieldName(String line) {
		final String fieldName = "FieldName=";
		final String semicolon = ";";
		return line.substring(line.indexOf(fieldName) + fieldName.length(), line.indexOf(semicolon));
	}

	private String getOriginalName(String line) {
		final String originalName = "OrigName=";
		final String semicolon = ";";
		final int originalNamePosition = line.indexOf(originalName);
		return line.substring(originalNamePosition + originalName.length(), line.indexOf(semicolon, originalNamePosition));
	}

	private String getValue(String line) {
		final String value = "Value=";
		return line.substring(line.indexOf(value) + value.length());
	}

	private boolean isDataDefinitionFileProcessingBlock(String line) {
		return line.contains("DATA DEFINITION FILE PROCESSING");
	}

	private boolean containsMessage(String line) {
		return line.startsWith("ERROR:") || line.startsWith("WARNING:") || line.startsWith("INFORM:");
	}

	private DataObjectStatus getStatus(String line) {
		if (line.startsWith("ERROR:")) {
			return DataObjectStatus.ERROR;
		}
		if (line.startsWith("WARNING:")) {
			return DataObjectStatus.WARNING;
		}
		if (line.startsWith("INFORM:")) {
			return DataObjectStatus.SUCCESS;
		}
		return null;
	}

	private String getMessage(String line) {
		final String colon = ":";
		return line.substring(line.indexOf(colon) + 2).trim();
	}
}
