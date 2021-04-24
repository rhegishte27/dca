package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.api.project.dto.BackupIntervalDto;
import com.equisoft.dca.api.project.mapper.BackupIntervalMapper;
import com.equisoft.dca.backend.project.model.BackupInterval;

@ExtendWith(MockitoExtension.class)
class BackupIntervalResourceTest {

	private BackupIntervalResource backupIntervalResource;

	@Mock
	private BackupIntervalMapper mapper;

	@BeforeEach
	void setUp() {
		backupIntervalResource = new BackupIntervalResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenBackupIntervals_whenFindAll_thenReturnStatusCodeOkAndBodyWithBackupIntervalList() {
			//given
			List<BackupInterval> backupIntervals = Arrays.asList(BackupInterval.values());
			Mockito.when(mapper.toDtoList(backupIntervals)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<BackupIntervalDto>> actual = backupIntervalResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<BackupIntervalDto> getLstDto() {
			return List.of(
					BackupIntervalDto.builder().id(1).name("test").build(),
					BackupIntervalDto.builder().id(2).name("test2").build()
			);
		}
	}
}
