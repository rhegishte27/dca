package com.equisoft.dca.backend.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.equisoft.dca.backend.project.model.Project;

public class ValidBackupValidator implements ConstraintValidator<ValidBackup, Project> {

	@Override
	public boolean isValid(Project project, ConstraintValidatorContext constraintValidatorContext) {
		if (project == null) {
			return false;
		}

		if (!project.getIsBackupEnabled()) {
			return project.getBackupInterval() == null && project.getNumberOfBackupIntervals() == null &&
					project.getBackupKeepInterval() == null && project.getNumberOfBackupKeepIntervals() == null;
		}

		return project.getBackupInterval() != null && project.getNumberOfBackupIntervals() != null && project.getNumberOfBackupIntervals() > 0 &&
				project.getBackupKeepInterval() != null && project.getNumberOfBackupKeepIntervals() != null && project.getNumberOfBackupKeepIntervals() > 0;
	}
}
