package com.equisoft.dca.backend.project.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.project.model.BackupKeepInterval;

@Converter(autoApply = true)
public class BackupKeepIntervalConverter extends BaseEnumConverter<BackupKeepInterval, Integer> {

	BackupKeepIntervalConverter() {
		super(BackupKeepInterval.class);
	}
}
