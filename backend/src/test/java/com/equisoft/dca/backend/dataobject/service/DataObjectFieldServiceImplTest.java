package com.equisoft.dca.backend.dataobject.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataObjectFieldServiceImplTest {

	/*
	@Mock
	private DataObjectFieldRepository repository;

	private DataObjectFieldService service;

	@BeforeEach
	void init() {
		service = new DataObjectFieldServiceImpl(repository);
	}

	@Nested
	class ExceptionWhenSaveDataObjectResultNotValid {

		@Test
		void givenDataObjectResultNotValid_whenSave_thenThrowIllegalArgumentException() {
			//given
			Class expected = IllegalArgumentException.class;

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.save(DataObjectFile.builder().valid(Boolean.FALSE).build()));

			//then
			Mockito.verify(repository, Mockito.times(0)).deleteByDataObjectId(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).saveAll(ArgumentMatchers.anyIterable());

			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class ResultContentNoFields {

		@Test
		void givenNoLinesWithField_whenSave_thenReturnEmptyList() {
			//given

			//when
			List<DataObjectField> actual = service.save(getDataObjectResultNoFields());

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteByDataObjectId(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).saveAll(ArgumentMatchers.anyIterable());

			Assertions.assertThat(actual).isEmpty();
		}

		DataObjectFile getDataObjectResultNoFields() {
			return DataObjectFile.builder()
					.dataObject(DataObject.builder().id(1).build())
					.valid(Boolean.TRUE)
					.content("*************************************************************"
							+ System.lineSeparator()
							+ "* Copyright 2021 Universal Conversion Technologies"
							+ System.lineSeparator()
							+ "*                     Patents Pending"
							+ System.lineSeparator()
							+ "*************************************************************"
							+ System.lineSeparator()
							+ "* COPYPRC PC Version 12.0 - 02/11/21 21:39:17"
							+ System.lineSeparator()
							+ "*************************************************************"
							+ System.lineSeparator()
							+ " "
							+ System.lineSeparator()
							+ "Compiled for the IBM 370 and other IBM OS/390 computers, COMP equates to BINARY")
					.build();
		}
	}

	@Nested
	class ResultContentWithFieldB {

		@Test
		void givenFieldB_whenSave_thenReturnList() {
			//given
			List<DataObjectField> expected = List.of(
					DataObjectField.builder()
							.name("TRCNVTRX")
							.orderNumber(1)
							.dataType(FieldDataType.NO_MASK)
							.ddlType(FieldDdlType.NONE)
							.level(1)
							.build(),
					DataObjectField.builder()
							.name("CTR-ID")
							.orderNumber(2)
							.dataType(FieldDataType.ALPHA)
							.ddlType(FieldDdlType.UNIQUE_ID)
							.level(5)
							.build(),
					DataObjectField.builder()
							.name("CTR-PUARFACEAMTSURRHDR")
							.orderNumber(3)
							.dataType(FieldDataType.FIXED)
							.ddlType(FieldDdlType.NUMERIC)
							.level(5)
							.build());

			Mockito.when(repository.saveAll(ArgumentMatchers.anyIterable())).thenReturn(expected);

			//when
			List<DataObjectField> actual = service.save(getDataObjectResultWithFieldsB());

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteByDataObjectId(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).saveAll(ArgumentMatchers.anyIterable());

			Assertions.assertThat(actual)
					.hasSameSizeAs(expected)
					.extracting(DataObjectField::getName)
					.containsExactly("TRCNVTRX", "CTR-ID", "CTR-PUARFACEAMTSURRHDR");
		}

		DataObjectFile getDataObjectResultWithFieldsB() {
			return DataObjectFile.builder()
					.dataObject(DataObject.builder().id(1).build())
					.valid(Boolean.TRUE)
					.content("                                                            01 TRCNVTRX - NO MASK                                               "
							+ "                                                                        ***CDWS TRCNVTRX B TRCNVTRX                         NO "
							+ "MASK                                                                                                                           "
							+ "                                                                                                                               "
							+ "                                                                                                              "
							+ System.lineSeparator()
							+ "                                                            05 CTR-ID - ALPHA                                                  "
							+ "                                                                         ***CDWS TRCNVTRX B CTR-ID                           "
							+ "ALPHA             PIC X(38)                                                                                                    "
							+ "                                                                                                                               "
							+ "                    UNIQUEID                                                                                     "
							+ System.lineSeparator()
							+ "                                                            05 CTR-PUARFACEAMTSURRHDR - FIXED SIGNED                           "
							+ "                                                                         ***CDWS TRCNVTRX B CTR-PUARFACEAMTSURRHDR           "
							+ "FIXED             PIC S9(16)V9(2)                                                                                              "
							+ "                                                                                                                               "
							+ "                    NUMERIC                                                                                      ")
					.build();
		}
	}

	@Nested
	class ResultContentWithFieldsAAndB {

		@Test
		void givenFieldsAAndB_whenSave_thenReturnList() {
			//given
			List<DataObjectField> expected = List.of(
					DataObjectField.builder()
							.name("CTR-ID")
							.orderNumber(1)
							.dataType(FieldDataType.ALPHA)
							.ddlType(FieldDdlType.UNIQUE_ID)
							.level(5)
							.build(),
					DataObjectField.builder()
							.name("CTR-PUARFACEAMTSURRHDR")
							.orderNumber(2)
							.dataType(FieldDataType.FIXED)
							.ddlType(FieldDdlType.NUMERIC)
							.level(5)
							.build());

			Mockito.when(repository.saveAll(ArgumentMatchers.anyIterable())).thenReturn(expected);

			//when
			List<DataObjectField> actual = service.save(getDataObjectResultWithFieldsB());

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteByDataObjectId(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).saveAll(ArgumentMatchers.anyIterable());

			Assertions.assertThat(actual)
					.hasSameSizeAs(expected)
					.extracting(DataObjectField::getName)
					.containsExactly("CTR-ID", "CTR-PUARFACEAMTSURRHDR");
		}

		DataObjectFile getDataObjectResultWithFieldsB() {
			return DataObjectFile.builder()
					.dataObject(DataObject.builder().id(1).build())
					.valid(Boolean.TRUE)
					.content("                                                            01 TRCNVTRX - NO MASK                                               "
							+ "                                                                        ***CDWS TRCNVTRX A TRCNVTRX                         NO "
							+ "MASK                                                                                                                           "
							+ "                                                                                                                               "
							+ "                                                                                                              "
							+ System.lineSeparator()
							+ "                                                            05 CTR-ID - ALPHA                                                  "
							+ "                                                                         ***CDWS TRCNVTRX B CTR-ID                           "
							+ "ALPHA             PIC X(38)                                                                                                    "
							+ "                                                                                                                               "
							+ "                    UNIQUEID                                                                                     "
							+ System.lineSeparator()
							+ "                                                            05 CTR-PUARFACEAMTSURRHDR - FIXED SIGNED                           "
							+ "                                                                         ***CDWS TRCNVTRX B CTR-PUARFACEAMTSURRHDR           "
							+ "FIXED             PIC S9(16)V9(2)                                                                                              "
							+ "                                                                                                                               "
							+ "                    NUMERIC                                                                                      ")
					.build();
		}
	}

	@Nested
	class ResultContentWithFieldsAAndBAndFieldname {

		@Test
		void givenFieldsAAndBAndFieldname_whenSave_thenReturnList() {
			//given
			List<DataObjectField> expected = List.of(
					DataObjectField.builder()
							.name("TRCNVTRX")
							.orderNumber(1)
							.dataType(FieldDataType.NO_MASK)
							.ddlType(FieldDdlType.NONE)
							.level(1)
							.originalName("TRCNVTRX-Test")
							.build(),
					DataObjectField.builder()
							.name("CTR-ID")
							.orderNumber(2)
							.dataType(FieldDataType.ALPHA)
							.ddlType(FieldDdlType.UNIQUE_ID)
							.level(5)
							.originalName("CTR-ID-Test")
							.build(),
					DataObjectField.builder()
							.name("CTR-PUARFACEAMTSURRHDR")
							.orderNumber(3)
							.dataType(FieldDataType.FIXED)
							.ddlType(FieldDdlType.NUMERIC)
							.level(5)
							.originalName("CTR-PUARFACEAMTSURRHDR-Test")
							.build());

			Mockito.when(repository.saveAll(ArgumentMatchers.anyIterable())).thenReturn(expected);

			//when
			List<DataObjectField> actual = service.save(getDataObjectResultWithFields());

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteByDataObjectId(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).saveAll(ArgumentMatchers.anyIterable());

			Assertions.assertThat(actual)
					.hasSameSizeAs(expected)
					.extracting(DataObjectField::getName)
					.containsExactly("TRCNVTRX", "CTR-ID", "CTR-PUARFACEAMTSURRHDR");

			Assertions.assertThat(actual)
					.hasSameSizeAs(expected)
					.extracting(DataObjectField::getOriginalName)
					.containsExactly("TRCNVTRX-Test", "CTR-ID-Test", "CTR-PUARFACEAMTSURRHDR-Test");
		}

		DataObjectFile getDataObjectResultWithFields() {
			return DataObjectFile.builder()
					.dataObject(DataObject.builder().id(1).build())
					.valid(Boolean.TRUE)
					.content("                                                                                                                                "
							+ "                                                                        ***CDWS TRCNVTRX A"
							+ System.lineSeparator()
							+ "   DATA DEFINITION FILE PROCESSING IN PROGRESS"
							+ System.lineSeparator()
							+ "INFORM: No existing DD file found: creating 'C:\\USERS\\VSOLANO\\TOOLS\\APACHE-TOMCAT-9.0"
							+ ".37-INTELLIJ\\TEMP\\IMPORT_DATA_OBJECT_16911204486624070404\\COPYDD.DAT'"
							+ System.lineSeparator()
							+ "                                                            01 TRCNVTRX - NO MASK                                              "
							+ "                                                                         ***CDWS TRCNVTRX B TRCNVTRX                         NO"
							+ " MASK                                                                                                                          "
							+ "                                                                                                                               "
							+ "                                                                                                               "
							+ System.lineSeparator()
							+ "                                                            05 CTR-ID - ALPHA                                                  "
							+ "                                                                         ***CDWS TRCNVTRX B CTR-ID                           "
							+ "ALPHA             PIC X(38)                                                                                                    "
							+ "                                                                                                                               "
							+ "                    UNIQUEID                                                                                     "
							+ System.lineSeparator()
							+ "                                                            05 CTR-PUARFACEAMTSURRHDR - FIXED SIGNED                           "
							+ "                                                                         ***CDWS TRCNVTRX B CTR-PUARFACEAMTSURRHDR           "
							+ "FIXED             PIC S9(16)V9(2)                                                                                              "
							+ "                                                                                                                               "
							+ "                    NUMERIC                                                                                      "
							+ System.lineSeparator()
							+ "FieldName=TRCNVTRX;OrigName=TRCNVTRX-Test;Value="
							+ System.lineSeparator()
							+ "FieldName=CTR-ID;OrigName=CTR-ID-Test;Value="
							+ System.lineSeparator()
							+ "FieldName=CTR-PUARFACEAMTSURRHDR;OrigName=CTR-PUARFACEAMTSURRHDR-Test;Value=")
					.build();
		}
	}

	@Nested
	class DeleteByDataObjectId {
		@Test
		void givenDataObjectIdNull_whenDeleteByDataObjectId_throwNullPointerException() {
			// given
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> service.deleteByDataObjectId(null));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenDataObjectIdNotNull_whenDeleteByDataObjectId_callRepositoryDeleteByDataObjectId() {
			// given

			// when
			service.deleteByDataObjectId(ArgumentMatchers.anyInt());

			// then
			Mockito.verify(repository, Mockito.times(1)).deleteByDataObjectId(ArgumentMatchers.anyInt());
		}
	}
	*/
}
