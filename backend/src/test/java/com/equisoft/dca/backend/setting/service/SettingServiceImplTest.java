package com.equisoft.dca.backend.setting.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.exception.setting.ApplicationSettingCommonFolderException;
import com.equisoft.dca.backend.exception.setting.ApplicationSettingDefaultFolderException;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.setting.dao.SettingRepository;
import com.equisoft.dca.backend.setting.model.Setting;

@ExtendWith(MockitoExtension.class)
class SettingServiceImplTest {
	@Mock
	private SettingRepository repository;
	@Mock
	private FileSystemService fileSystemService;

	private SettingService service;

	@BeforeEach
	void setUp() {
		service = new SettingServiceImpl(repository, fileSystemService);
	}

	@Nested
	class Save {
		@Nested
		class GivenRepositoryFindAllReturnEmpty {
			private Setting setting;
			@Mock
			private Path commonFolder;
			@Mock
			private Path importFolder;
			@Mock
			private Path exportFolder;
			@Mock
			private Path downloadFolder;

			@BeforeEach
			void setUp() {
				setting = createSetting(null);
				Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
			}

			@Nested
			class GivenAllFoldersPathValid {

				@BeforeEach
				void setUp() {
					Mockito.doReturn(commonFolder).when(fileSystemService).getPath(setting.getCommonFolder());
					Mockito.doReturn(importFolder).when(fileSystemService).getPath(setting.getDefaultImportFolder());
					Mockito.doReturn(exportFolder).when(fileSystemService).getPath(setting.getDefaultExportFolder());
					Mockito.doReturn(downloadFolder).when(fileSystemService).getPath(setting.getDefaultDownloadFolder());

					Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getCommonFolder());
					Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
					Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
					Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());

					Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(importFolder, commonFolder);
					Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(exportFolder, commonFolder);
					Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(downloadFolder, commonFolder);
				}

				@Test
				void givenFileSystemServiceThrowIOException_whenSave_throwRuntimeException() throws IOException {
					// given
					Mockito.doThrow(IOException.class).when(fileSystemService).createDirectories(ArgumentMatchers.any(Path.class));

					Class expected = RuntimeException.class;

					// when
					Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

					// then
					Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
					Assertions.assertThat(actual).isInstanceOf(expected);
				}

				@Test
				void givenSetting_whenSave_shouldCallRepositorySaveAndCreateAllFoldersAndReturnSetting() throws IOException {
					// given
					Setting settingFromRepo = createSetting(1);
					Mockito.when(repository.save(setting)).thenReturn(settingFromRepo);

					// when
					Setting actual = service.save(setting);

					//then
					Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Setting.class));
					Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(commonFolder);
					Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(importFolder);
					Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(exportFolder);
					Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(downloadFolder);
					Assertions.assertThat(actual)
							.isNotNull()
							.isEqualTo(settingFromRepo);
				}
			}

			@Nested
			class GivenFoldersPathNotValid {
				@Test
				void givenCommonFolderNotValid_whenSave_thenThrowApplicationSettingCommonFolderException() {
					// given
					Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getCommonFolder());
					Class expected = ApplicationSettingCommonFolderException.class;

					// when
					Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

					// then
					Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
					Assertions.assertThat(actual).isInstanceOf(expected);
				}

				@Nested
				class GivenCommonFolderValid {
					@BeforeEach
					void setUp() {
						Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getCommonFolder());
						Mockito.doReturn(commonFolder).when(fileSystemService).getPath(setting.getCommonFolder());
					}

					@Test
					void givenImportFolderNotValid_whenSave_thenThrowApplicationSettingDefaultFolderException() {
						// given
						Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
						Class expected = ApplicationSettingDefaultFolderException.class;

						// when
						Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

						// then
						Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
						Assertions.assertThat(actual).isInstanceOf(expected);
					}

					@Test
					void givenImportFolderValidNotSubFolderCommonFolder_whenSave_thenThrowApplicationDefaultFolderException() {
						// given
						Mockito.doReturn(importFolder).when(fileSystemService).getPath(setting.getDefaultImportFolder());
						Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
						Mockito.doReturn(false).when(fileSystemService).isPathSubFolder(importFolder, commonFolder);
						Class expected = ApplicationSettingDefaultFolderException.class;

						// when
						Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

						// then
						Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
						Assertions.assertThat(actual).isInstanceOf(expected);
					}

					@Nested
					class GivenImportFolderValid {
						@BeforeEach
						void setUp() {
							Mockito.doReturn(importFolder).when(fileSystemService).getPath(setting.getDefaultImportFolder());
							Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
							Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(importFolder, commonFolder);
						}

						@Test
						void givenExportFolderNotValid_whenSave_thenThrowApplicationSettingDefaultFolderException() {
							// given
							Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
							Class expected = ApplicationSettingDefaultFolderException.class;

							// when
							Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

							// then
							Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
							Assertions.assertThat(actual).isInstanceOf(expected);
						}

						@Test
						void givenExportFolderValidNotSubFolderCommonFolder_whenSave_thenThrowApplicationDefaultFolderException() {
							// given
							Mockito.doReturn(exportFolder).when(fileSystemService).getPath(setting.getDefaultExportFolder());
							Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
							Mockito.doReturn(false).when(fileSystemService).isPathSubFolder(exportFolder, commonFolder);
							Class expected = ApplicationSettingDefaultFolderException.class;

							// when
							Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

							// then
							Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
							Assertions.assertThat(actual).isInstanceOf(expected);
						}

						@Nested
						class GivenExportFolderValid {
							@BeforeEach
							void setUp() {
								Mockito.doReturn(exportFolder).when(fileSystemService).getPath(setting.getDefaultExportFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(exportFolder, commonFolder);
							}

							@Test
							void givenDownloadFolderNotValid_whenSave_thenThrowApplicationSettingDefaultFolderException() {
								// given
								Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
								Class expected = ApplicationSettingDefaultFolderException.class;

								// when
								Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

								// then
								Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
								Assertions.assertThat(actual).isInstanceOf(expected);
							}

							@Test
							void givenDownloadFolderValidNotSubFolderCommonFolder_whenSave_thenThrowApplicationDefaultFolderException() {
								// given
								Mockito.doReturn(downloadFolder).when(fileSystemService).getPath(setting.getDefaultDownloadFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
								Mockito.doReturn(false).when(fileSystemService).isPathSubFolder(downloadFolder, commonFolder);
								Class expected = ApplicationSettingDefaultFolderException.class;

								// when
								Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

								// then
								Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
								Assertions.assertThat(actual).isInstanceOf(expected);
							}

							@Test
							void givenDownloadFolderValidSubFolderCommonFolder_whenSave_noExceptionThrow() {
								// given
								Mockito.doReturn(downloadFolder).when(fileSystemService).getPath(setting.getDefaultDownloadFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(downloadFolder, commonFolder);

								// when
								Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

								// then
								Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Setting.class));
								Assertions.assertThat(actual).isNull();
							}
						}
					}
				}
			}
		}

		@Nested
		class GivenRepositoryFindAllReturnLst {
			@Test
			void givenSetting_whenSave_shouldThrowEntityAlreadyExistsException() {
				// given
				Class expected = EntityAlreadyExistsException.class;
				Setting setting = createSetting(null);
				Mockito.when(repository.findAll()).thenReturn(Arrays.asList(createSetting(1)));

				// when
				Throwable actual = Assertions.catchThrowable(() -> service.save(setting));

				//then
				Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
				Assertions.assertThat(actual).isInstanceOf(expected);
			}
		}
	}

	@Nested
	class Update {
		@Nested
		class GivenRepositoryReturnEmpty {
			private Setting setting;

			@BeforeEach
			void setUp() {
				setting = createSetting(1);
				Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
			}

			@Test
			void givenSetting_whenUpdate_shouldThrowEntityNotFoundException() {
				// given
				Class expected = EntityNotFoundException.class;

				// when
				Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

				//then
				Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
				Assertions.assertThat(actual).isInstanceOf(expected);
			}
		}

		@Nested
		class GivenRepositoryReturnLst {
			private Setting setting;
			private Setting settingFromRepo;

			@Nested
			class GivenSystemIdNull {
				@BeforeEach
				void setUp() {
					setting = createSetting(null);
					settingFromRepo = createSetting(2);
					Mockito.when(repository.findAll()).thenReturn(Arrays.asList(settingFromRepo));
				}

				@Test
				void givenSystemIdNullAndRepositoryFindAllReturnLst_whenUpdate_shouldThrowEntityNotFoundException() {
					// given
					Class expected = EntityNotFoundException.class;

					// when
					Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

					//then
					Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
					Assertions.assertThat(actual).isInstanceOf(expected);
				}
			}

			@Nested
			class GivenLstDifferentId {
				@BeforeEach
				void setUp() {
					setting = createSetting(1);
					settingFromRepo = createSetting(2);
					Mockito.when(repository.findAll()).thenReturn(Arrays.asList(settingFromRepo));
				}

				@Test
				void givenSettingAndRepositoryFindAllReturnLstDifferentId_whenUpdate_shouldThrowEntityNotFoundException() {
					// given
					Class expected = EntityNotFoundException.class;

					// when
					Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

					//then
					Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
					Assertions.assertThat(actual).isInstanceOf(expected);
				}
			}

			@Nested
			class GivenLstSameId {
				@Mock
				private Path commonFolder;
				@Mock
				private Path importFolder;
				@Mock
				private Path exportFolder;
				@Mock
				private Path downloadFolder;

				@BeforeEach
				void setUp() {
					setting = createSetting(1);
					settingFromRepo = createSetting(1);
					Mockito.when(repository.findAll()).thenReturn(Arrays.asList(settingFromRepo));
				}

				@Nested
				class GivenAllFoldersPathValid {

					@BeforeEach
					void setUp() {
						Mockito.doReturn(commonFolder).when(fileSystemService).getPath(setting.getCommonFolder());
						Mockito.doReturn(importFolder).when(fileSystemService).getPath(setting.getDefaultImportFolder());
						Mockito.doReturn(exportFolder).when(fileSystemService).getPath(setting.getDefaultExportFolder());
						Mockito.doReturn(downloadFolder).when(fileSystemService).getPath(setting.getDefaultDownloadFolder());

						Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
						Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
						Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getCommonFolder());
						Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());

						Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(importFolder, commonFolder);
						Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(exportFolder, commonFolder);
						Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(downloadFolder, commonFolder);
					}

					@Test
					void givenFileSystemServiceThrowIOException_whenUpdate_throwRuntimeException() throws IOException {
						// given
						Mockito.doThrow(IOException.class).when(fileSystemService).createDirectories(ArgumentMatchers.any(Path.class));

						Class expected = RuntimeException.class;

						// when
						Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

						// then
						Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
						Assertions.assertThat(actual).isInstanceOf(expected);
					}

					@Test
					void givenSettingAndRepositoryFindAllReturnLstSameId_whenUpdate_shouldCallRepositorySaveAndCreateDirectoriesAndReturnSetting()
							throws IOException {
						// given
						Mockito.when(repository.save(setting)).thenReturn(settingFromRepo);

						// when
						Setting actual = service.update(setting);

						//then
						Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Setting.class));
						Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(commonFolder);
						Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(importFolder);
						Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(exportFolder);
						Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(downloadFolder);
						Assertions.assertThat(actual)
								.isNotNull()
								.isEqualTo(settingFromRepo);
					}
				}

				@Nested
				class GivenFoldersPathNotValid {
					@Test
					void givenCommonFolderNotValid_whenSave_thenThrowApplicationSettingCommonFolderException() {
						// given
						Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getCommonFolder());
						Class expected = ApplicationSettingCommonFolderException.class;

						// when
						Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

						// then
						Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
						Assertions.assertThat(actual).isInstanceOf(expected);
					}

					@Nested
					class GivenCommonFolderValid {
						@BeforeEach
						void setUp() {
							Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getCommonFolder());
							Mockito.doReturn(commonFolder).when(fileSystemService).getPath(setting.getCommonFolder());
						}

						@Test
						void givenImportFolderNotValid_whenSave_thenThrowApplicationSettingDefaultFolderException() {
							// given
							Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
							Class expected = ApplicationSettingDefaultFolderException.class;

							// when
							Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

							// then
							Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
							Assertions.assertThat(actual).isInstanceOf(expected);
						}

						@Test
						void givenImportFolderValidNotSubFolderCommonFolder_whenSave_thenThrowApplicationDefaultFolderException() {
							// given
							Mockito.doReturn(importFolder).when(fileSystemService).getPath(setting.getDefaultImportFolder());
							Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
							Mockito.doReturn(false).when(fileSystemService).isPathSubFolder(importFolder, commonFolder);
							Class expected = ApplicationSettingDefaultFolderException.class;

							// when
							Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

							// then
							Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
							Assertions.assertThat(actual).isInstanceOf(expected);
						}

						@Nested
						class GivenImportFolderValid {
							@BeforeEach
							void setUp() {
								Mockito.doReturn(importFolder).when(fileSystemService).getPath(setting.getDefaultImportFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultImportFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(importFolder, commonFolder);
							}

							@Test
							void givenExportFolderNotValid_whenSave_thenThrowApplicationSettingDefaultFolderException() {
								// given
								Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
								Class expected = ApplicationSettingDefaultFolderException.class;

								// when
								Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

								// then
								Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
								Assertions.assertThat(actual).isInstanceOf(expected);
							}

							@Test
							void givenExportFolderValidNotSubFolderCommonFolder_whenSave_thenThrowApplicationDefaultFolderException() {
								// given
								Mockito.doReturn(exportFolder).when(fileSystemService).getPath(setting.getDefaultExportFolder());
								Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
								Mockito.doReturn(false).when(fileSystemService).isPathSubFolder(exportFolder, commonFolder);
								Class expected = ApplicationSettingDefaultFolderException.class;

								// when
								Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

								// then
								Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
								Assertions.assertThat(actual).isInstanceOf(expected);
							}

							@Nested
							class GivenExportFolderValid {
								@BeforeEach
								void setUp() {
									Mockito.doReturn(exportFolder).when(fileSystemService).getPath(setting.getDefaultExportFolder());
									Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultExportFolder());
									Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(exportFolder, commonFolder);
								}

								@Test
								void givenDownloadFolderNotValid_whenSave_thenThrowApplicationSettingDefaultFolderException() {
									// given
									Mockito.doReturn(false).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
									Class expected = ApplicationSettingDefaultFolderException.class;

									// when
									Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

									// then
									Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
									Assertions.assertThat(actual).isInstanceOf(expected);
								}

								@Test
								void givenDownloadFolderValidNotSubFolderCommonFolder_whenSave_thenThrowApplicationDefaultFolderException() {
									// given
									Mockito.doReturn(downloadFolder).when(fileSystemService).getPath(setting.getDefaultDownloadFolder());
									Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
									Mockito.doReturn(false).when(fileSystemService).isPathSubFolder(downloadFolder, commonFolder);
									Class expected = ApplicationSettingDefaultFolderException.class;

									// when
									Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

									// then
									Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Setting.class));
									Assertions.assertThat(actual).isInstanceOf(expected);
								}

								@Test
								void givenDownloadFolderValidSubFolderCommonFolder_whenSave_noExceptionThrow() {
									// given
									Mockito.doReturn(downloadFolder).when(fileSystemService).getPath(setting.getDefaultDownloadFolder());
									Mockito.doReturn(true).when(fileSystemService).isPathValid(setting.getDefaultDownloadFolder());
									Mockito.doReturn(true).when(fileSystemService).isPathSubFolder(downloadFolder, commonFolder);

									// when
									Throwable actual = Assertions.catchThrowable(() -> service.update(setting));

									// then
									Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Setting.class));
									Assertions.assertThat(actual).isNull();
								}
							}
						}
					}
				}
			}
		}
	}

	@Nested
	class Get {
		@Test
		void givenFindAllReturnListSetting_whenGet_thenReturnTheFirstSettingInTheList() {
			// given
			List<Setting> lst = Arrays.asList(createSetting(1), createSetting(2), createSetting(3));
			Mockito.when(repository.findAll()).thenReturn(lst);
			Setting firstElement = lst.get(0);

			// when
			Optional<Setting> actual = service.get();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(Optional.of(firstElement));
		}

		@Test
		void givenFindAllReturnListEmpty_whenGet_thenReturnOptionalEmpty() {
			// given
			List<Setting> lst = Collections.emptyList();
			Mockito.when(repository.findAll()).thenReturn(lst);

			// when
			Optional<Setting> actual = service.get();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(Optional.empty());
		}
	}

	private Setting createSetting(Integer id) {
		return Setting.builder()
				.id(id)
				.language(Language.SPANISH)
				.commonFolder("test")
				.defaultImportFolder("test/import")
				.defaultExportFolder("test/export")
				.defaultDownloadFolder("test/download")
				.build();
	}
}
