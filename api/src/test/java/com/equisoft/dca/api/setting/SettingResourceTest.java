package com.equisoft.dca.api.setting;

import java.net.URI;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.setting.dto.SettingDto;
import com.equisoft.dca.api.setting.mapper.SettingMapper;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.setting.model.Setting;
import com.equisoft.dca.backend.setting.service.SettingService;

@ExtendWith(MockitoExtension.class)
class SettingResourceTest {
	@Mock
	private SettingService settingService;
	@Mock
	private SettingMapper mapper;

	private SettingResource settingResource;

	@BeforeEach
	void setUp() {
		settingResource = new SettingResource(settingService, mapper);
	}

	@Nested
	class Add {
		@Test
		void givenValidSetting_whenAdd_thenReturnStatusCodeCreatedAndSetting() {
			// given
			Setting setting = createSetting(1);
			SettingDto settingDto = createSettingDto(setting);

			Mockito.when(settingService.save(setting)).thenReturn(setting);
			Mockito.when(mapper.toEntity(settingDto)).thenReturn(setting);
			Mockito.when(mapper.toDto(setting)).thenReturn(settingDto);

			ResponseEntity expected = ResponseEntity.created(URI.create("/settings/" + setting.getId())).body(settingDto);

			// when
			ResponseEntity<SettingDto> actual = settingResource.add(settingDto);

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getHeaders, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getHeaders(), expected.getBody());
		}
	}

	@Nested
	class Update {
		@Test
		void givenSetting_whenUpdate_thenReturnStatusCodeOkAndNoResponseBody() {
			// given
			Setting setting = createSetting(1);
			SettingDto settingDto = createSettingDto(setting);

			Mockito.when(mapper.toEntity(settingDto)).thenReturn(setting);

			ResponseEntity expected = ResponseEntity.ok().build();

			// when
			ResponseEntity<Void> actual = settingResource.update(1, settingDto);

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class Get {
		@Test
		void givenServiceReturnSetting_whenGet_returnStatusCodeOkAndSetting() {
			// given
			Setting setting = createSetting(1);
			SettingDto settingDto = createSettingDto(setting);

			Mockito.when(settingService.get()).thenReturn(Optional.of(setting));
			Mockito.when(mapper.toDto(setting)).thenReturn(settingDto);

			ResponseEntity expected = ResponseEntity.ok().body(settingDto);

			// when
			ResponseEntity<SettingDto> actual = settingResource.get();

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenServiceReturnEmpty_whenGet_returnStatusCodeOkAndNewSetting() {
			// given
			Setting setting = Setting.builder().build();
			SettingDto settingDto = createSettingDto(setting);

			Mockito.when(settingService.get()).thenReturn(Optional.empty());
			Mockito.when(mapper.toDto(setting)).thenReturn(settingDto);

			ResponseEntity expected = ResponseEntity.ok().body(settingDto);

			// when
			ResponseEntity<SettingDto> actual = settingResource.get();

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private Setting createSetting(Integer id) {
		return Setting.builder()
				.id(id)
				.language(Language.SPANISH)
				.tokenDuration(20)
				.commonFolder("common")
				.defaultImportFolder("common/import")
				.defaultExportFolder("common/export")
				.defaultDownloadFolder("common/download")
				.build();
	}

	private SettingDto createSettingDto(Setting setting) {
		return SettingDto.builder()
				.id(setting.getId())
				.language(createLanguageDto(setting.getLanguage()))
				.tokenDuration(setting.getTokenDuration())
				.commonFolder(setting.getCommonFolder())
				.defaultDownloadFolder(setting.getDefaultDownloadFolder())
				.defaultExportFolder(setting.getDefaultExportFolder())
				.defaultImportFolder(setting.getDefaultImportFolder())
				.build();
	}

	private LanguageDto createLanguageDto(Language language) {
		return language == null
				? null
				: LanguageDto.builder()
						.id(language.getId())
						.name(language.getName())
						.code(language.getCode())
						.build();
	}
}
