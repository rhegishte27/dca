package com.equisoft.dca.backend.project.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.project.model.BackupInterval;

@Converter(autoApply = true)
public class BackupIntervalConverter extends BaseEnumConverter<BackupInterval, Integer> {

	BackupIntervalConverter() {
		super(BackupInterval.class);
	}
}
