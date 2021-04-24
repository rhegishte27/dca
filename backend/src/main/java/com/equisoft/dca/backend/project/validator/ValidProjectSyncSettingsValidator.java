package com.equisoft.dca.backend.project.validator;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.equisoft.dca.backend.project.model.ProjectSyncSetting;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

public class ValidProjectSyncSettingsValidator implements ConstraintValidator<ValidProjectSyncSettings, Set<ProjectSyncSetting>> {
	@Override
	public boolean isValid(Set<ProjectSyncSetting> projectSyncSettings, ConstraintValidatorContext constraintValidatorContext) {
		if (projectSyncSettings == null
				|| projectSyncSettings.size() != TypeProjectElement.values().length) {
			return false;
		}

		Set projects = projectSyncSettings.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(p -> p.getId().getTypeProjectElement()))));

		return projects.size() == projectSyncSettings.size();
	}
}
