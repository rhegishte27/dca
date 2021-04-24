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

import com.equisoft.dca.api.project.dto.BackupKeepIntervalDto;
import com.equisoft.dca.api.project.mapper.BackupKeepIntervalMapper;
import com.equisoft.dca.backend.project.model.BackupKeepInterval;

@ExtendWith(MockitoExtension.class)
class BackupKeepIntervalResourceTest {

	private BackupKeepIntervalResource backupKeepIntervalResource;

	@Mock
	private BackupKeepIntervalMapper mapper;

	@BeforeEach
	void setUp() {
		this.backupKeepIntervalResource = new BackupKeepIntervalResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenBackupKeepIntervals_whenFindAll_thenReturnStatusCodeOkAndBodyWithBackupKeepIntervalList() {
			//given
			List<BackupKeepInterval> backupKeepIntervals = Arrays.asList(BackupKeepInterval.values());
			Mockito.when(mapper.toDtoList(backupKeepIntervals)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<BackupKeepIntervalDto>> actual = backupKeepIntervalResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<BackupKeepIntervalDto> getLstDto() {
			return List.of(
					BackupKeepIntervalDto.builder().id(1).name("test").build(),
					BackupKeepIntervalDto.builder().id(2).name("test2").build()
			);
		}
	}
}
