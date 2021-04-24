package com.equisoft.dca.backend.project.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidBackupValidator.class })
@Documented
public @interface ValidBackup {

	String message() default "";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
