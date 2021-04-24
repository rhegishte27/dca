package com.equisoft.dca.backend.dataobject.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.equisoft.dca.backend.dataobject.model.DataObject.DataObjectBuilder;

@ExtendWith(MockitoExtension.class)
class DataObjectTest {

	private DataObjectBuilder dataObjectBuilder;

	@BeforeEach
	void setUp() {
		dataObjectBuilder = DataObject.builder();
	}

	@Nested
	class IdentifierFormat {

		@ParameterizedTest
		@CsvSource({"tesT test , TEST TEST ", "Te123, TE123"})
		void givenUnformattedIdentifier_whenSet_thenUpperCase(String actual, String expected) {
			//given
			dataObjectBuilder.identifier(actual);

			//when
			DataObject dataObject = dataObjectBuilder.build();

			//then
			Assertions.assertThat(dataObject.getIdentifier()).isEqualTo(expected);
		}
	}
}
