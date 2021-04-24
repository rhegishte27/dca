package com.equisoft.dca.backend.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.equisoft.dca.backend.project.model.Compiler;
import com.equisoft.dca.backend.project.model.Project;

public class ValidCompilerValidator implements ConstraintValidator<ValidCompiler, Project> {

	@Override
	public boolean isValid(Project project, ConstraintValidatorContext constraintValidatorContext) {
		if (project == null || project.getCompiler() == null || project.getGeneratedCodeLanguage() == null) {
			return false;
		}

		switch (project.getGeneratedCodeLanguage()) {
			case JAVA:
				return project.getCompiler().equals(Compiler.GENERIC);
			case COBOL:
				return project.getCompiler().equals(Compiler.COBOL_II) || project.getCompiler().equals(Compiler.GENERIC) ||
						project.getCompiler().equals(Compiler.DOUBLE_BYTE) || project.getCompiler().equals(Compiler.FUJITSU) ||
						project.getCompiler().equals(Compiler.MICROFOCUS) || project.getCompiler().equals(Compiler.VISUAL_AGE);
			default:
				return false;
		}
	}
}
