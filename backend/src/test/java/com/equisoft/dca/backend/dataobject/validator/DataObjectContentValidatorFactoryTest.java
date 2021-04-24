package com.equisoft.dca.backend.dataobject.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.equisoft.dca.backend.dataobject.model.DataObjectType;

class DataObjectContentValidatorFactoryTest {

	private DataObjectContentValidatorFactory factory;

	@BeforeEach
	void setUp() {
		factory = new DataObjectContentValidatorFactory();
	}

	@Test
	void givenValidDataObjectType_whenCreateDataObjectValidator_thenReturnValidator() {
		//given
		Class expected = CobolDataObjectContentValidator.class;

		//when
		DataObjectContentValidator actual = factory.createDataObjectContentValidator(DataObjectType.COBOL_COPYBOOK);

		//then
		Assertions.assertThat(actual).isInstanceOf(expected);
	}

	@Test
	void givenInvalidDataObjectType_whenCreateDataObjectValidator_thenThrowIllegalArgumentException() {
		//given
		Class expected = IllegalArgumentException.class;

		//when
		Throwable actual = Assertions.catchThrowable(() -> factory.createDataObjectContentValidator(null));

		//then
		Assertions.assertThat(actual).isInstanceOf(expected);
	}
}
