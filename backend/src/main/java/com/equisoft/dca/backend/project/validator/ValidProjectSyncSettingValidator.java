package com.equisoft.dca.backend.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.equisoft.dca.backend.project.model.ProjectSyncSetting;

public class ValidProjectSyncSettingValidator implements ConstraintValidator<ValidProjectSyncSetting, ProjectSyncSetting> {
	@Override
	public boolean isValid(ProjectSyncSetting projectSyncSetting, ConstraintValidatorContext constraintValidatorContext) {
		if (projectSyncSetting == null) {
			return false;
		}

		if (projectSyncSetting.getIsSyncEnabled()) {
			return projectSyncSetting.getLocation() != null;
		}
		return true;
	}
}
