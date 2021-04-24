package com.equisoft.dca.backend.dataobject.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataObjectServiceImplTest {

	/*
	@Mock
	private DataObjectRepository repository;

	@Mock
	private UserService userService;

	@Mock
	private ImportDataObjectService importDataObjectService;

	@Mock
	private DataObjectContentValidatorFactory validatorFactory;

	@Mock
	private FtpService ftpService;

	@Mock
	private StringCompression stringCompression;

	private DataObjectService service;

	private User user;

	@BeforeEach
	void init() {
		service = new DataObjectServiceImpl(repository, userService, importDataObjectService, validatorFactory, ftpService, stringCompression);
		user = User.builder().id(1).identifier("user").build();
	}

	@Nested
	class validateDataObjects {

		@Mock
		private DataObjectContentValidator validator;

		@Test
		void givenListDataObjectWithValidContent_whenValidateDataObjects_returnDataObjectWithIsValidIsSet() throws IOException {
			// given
			Location location = Location.builder().build();
			List<DataObject> listDataObjects = List.of(DataObject.builder().type(DataObjectType.COBOL_COPYBOOK).content("test").build(),
					DataObject.builder().type(DataObjectType.JAVA_CLASS).content("test").build(),
					DataObject.builder().type(DataObjectType.DDL).content("test").build(),
					DataObject.builder().type(DataObjectType.MAINFRAME_ASSEMBLER).content("test").build(),
					DataObject.builder().type(DataObjectType.XML).content("test").build());

			Mockito.when(validatorFactory.createDataObjectContentValidator(ArgumentMatchers.any(DataObjectType.class))).thenReturn(validator);
			Mockito.when(validator.isValid(ArgumentMatchers.anyString())).thenReturn(true);

			// when
			List<DataObject> actual = service.validateDataObjects(dataObjectTypeMapper.toEntity(containerDto.getType()), location, listDataObjects);

			// then
			actual.forEach(d -> Assertions.assertThat(d.getStatus()).isEqualTo(DataObjectStatus.SUCCESS));
		}
	}

	@Nested
	class Create {
		@Test
		void givenFindByOriginalFileNameAndSystemIdReturnAList_whenCreate_createDataObjectWithDefaultIdentifier() {
			// given
			DataObject dataObjectToCreate = DataObject.builder().system(System.builder().id(1).build()).originalFileName("originalFileName").build();
			DataObject dataObjectFoundByOriginalFileName = DataObject.builder()
					.identifier("defaultIdentifier")
					.build();
			Mockito.when(repository.findByOriginalFileNameAndSystemIdOrderByDateTimeUploadedDesc(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
					.thenReturn(List.of(dataObjectFoundByOriginalFileName));

			// when
			List<DataObject> actual = service.create(List.of(dataObjectToCreate));

			// then
			actual.forEach(d -> {
				Assertions.assertThat(d.getIdentifier()).isEqualTo(dataObjectFoundByOriginalFileName.getIdentifier());
				Assertions.assertThat(d.getDescription()).isEqualTo(dataObjectToCreate.getOriginalFileName());
			});
		}

		@Test
		void givenFindByOriginalFileNameAndSystemIdReturnEmpty_whenCreate_createDataObjectWithDefaultIdentifierStringCompressor() {
			// given
			DataObject dataObjectToCreate = DataObject.builder().system(System.builder().id(1).build()).originalFileName("originalFileName").build();
			String defaultIdentifier = "defaultIdentifier";
			Mockito.when(repository.findByOriginalFileNameAndSystemIdOrderByDateTimeUploadedDesc(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
					.thenReturn(Collections.emptyList());
			Mockito.when(stringCompression
					.compress(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyList(), ArgumentMatchers.anyBoolean(),
							ArgumentMatchers.anyBoolean()))
					.thenReturn(Optional.of(defaultIdentifier));

			// when
			List<DataObject> actual = service.create(List.of(dataObjectToCreate));

			// then
			actual.forEach(d -> {
				Assertions.assertThat(d.getIdentifier()).isEqualTo(defaultIdentifier);
				Assertions.assertThat(d.getDescription()).isEqualTo(dataObjectToCreate.getOriginalFileName());
			});
		}
	}

	@Nested
	class Save {

		@Test
		void givenNonExistentDataObjectIdentifier_whenSave_thenReturnNewSystem() {
			//given
			DataObject dataObjectToSave = createDataObject(null, "identifier");
			DataObject dataObjectSaved = createInvalidDataObject(1, "identifier");
			DataObject expected = createInvalidDataObject(1, "identifier");

			Mockito.when(userService.findByIdentifier(user.getIdentifier())).thenReturn(Optional.ofNullable(user));
			Mockito.when(repository.findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
			Mockito.when(repository.save(ArgumentMatchers.any(DataObject.class))).thenReturn(dataObjectSaved);
			Mockito.when(importDataObjectService.execute(ArgumentMatchers.any(DataObject.class))).thenReturn(createInvalidImportDataObjectResult());

			//when
			DataObject actual = service.save(dataObjectToSave);

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(DataObject.class));
			Mockito.verify(importDataObjectService, Mockito.times(1)).execute(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenInvalidUserIdentifier_whenSave_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			DataObject dataObjectToSave = createDataObject(null, "identifier");

			Mockito.when(userService.findByIdentifier(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.save(dataObjectToSave));

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(0)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenExistingDataObjectIdentifier_whenSave_thenOverrideTheExistentDataObject() {
			//given
			DataObject dataObjectToSave = createDataObject(null, "identifier");
			DataObject existingDataObject = createDataObject(1, "identifier");
			DataObject expected = createDataObject(1, "identifier");

			Mockito.when(userService.findByIdentifier(user.getIdentifier())).thenReturn(Optional.ofNullable(user));
			Mockito.when(repository.findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
					.thenReturn(Optional.of(existingDataObject));
			Mockito.when(repository.save(dataObjectToSave)).thenReturn(expected);
			Mockito.when(importDataObjectService.execute(ArgumentMatchers.any(DataObject.class))).thenReturn(createInvalidImportDataObjectResult());

			//when
			DataObject actual = service.save(dataObjectToSave);

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(DataObject.class));
			Mockito.verify(importDataObjectService, Mockito.times(1)).execute(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class Update {

		@Test
		void givenExistingDataObjectIdAndIdentifier_whenUpdate_thenReturnDataObject() {
			//given
			DataObject dataObjectToUpdate = createDataObject(1, "identifier");
			DataObject dataObjectUpdated = createDataObject(1, "identifier");
			DataObject dataObjectWithSameIdAndIdentifier = createDataObject(1, "identifier");
			DataObject expected = createDataObject(1, "identifier");

			Mockito.when(userService.findByIdentifier(user.getIdentifier())).thenReturn(Optional.ofNullable(user));
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(dataObjectWithSameIdAndIdentifier));
			Mockito.when(repository.findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
					.thenReturn(Optional.of(dataObjectWithSameIdAndIdentifier));
			Mockito.when(repository.save(ArgumentMatchers.any(DataObject.class))).thenReturn(dataObjectUpdated);
			Mockito.when(importDataObjectService.execute(ArgumentMatchers.any(DataObject.class))).thenReturn(createValidImportDataObjectResult());

			//when
			DataObject actual = service.update(dataObjectToUpdate);

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(2)).save(ArgumentMatchers.any(DataObject.class));
			Mockito.verify(importDataObjectService, Mockito.times(1)).execute(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenInvalidUserIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			DataObject dataObjectToUpdate = createDataObject(1, "identifier");

			Mockito.when(userService.findByIdentifier(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(dataObjectToUpdate));

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(0)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenExistingDataObjectIdentifierWithDifferentId_whenUpdate_thenThrowEntityAlreadyExistsException() {
			//given
			Class expected = EntityAlreadyExistsException.class;
			DataObject dataObjectToUpdate = createDataObject(1, "identifier");
			DataObject dataObjectWithSameIdAndIdentifier = createDataObject(1, "identifier");
			DataObject dataObjectWithSameIdentifierAndDifferentId = createDataObject(2, "identifier");

			Mockito.when(userService.findByIdentifier(user.getIdentifier())).thenReturn(Optional.ofNullable(user));
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(dataObjectWithSameIdAndIdentifier));
			Mockito.when(repository.findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
					.thenReturn(Optional.of(dataObjectWithSameIdentifierAndDifferentId));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(dataObjectToUpdate));

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenNonExistentDataObjectId_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			DataObject dataObjectToUpdate = createDataObject(1, "identifier");

			Mockito.when(userService.findByIdentifier(user.getIdentifier())).thenReturn(Optional.ofNullable(user));
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(dataObjectToUpdate));

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenNonExistentDataObjectIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			DataObject dataObjectToUpdate = createDataObject(1, "identifier");
			DataObject existingDataObject = createDataObject(1, "other");

			Mockito.when(userService.findByIdentifier(user.getIdentifier())).thenReturn(Optional.ofNullable(user));
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(existingDataObject));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(dataObjectToUpdate));

			//then
			Mockito.verify(userService, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).findByIdentifierAndSystemId(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class DeleteById {

		@Test
		void givenExistingDataObjectId_whenDeleteById_thenVoid() {
			//given
			Mockito.doNothing().when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			service.deleteById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
		}

		@Test
		void givenNonExistentDataObjectId_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.deleteById(ArgumentMatchers.anyInt()));

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistingDataObjectId_whenFindById_thenReturnDataObject() {
			//given
			DataObject expected = createDataObject(1, "identifier");
			DataObject existingDataObject = createDataObject(1, "identifier");

			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(existingDataObject));

			//when
			Optional<DataObject> actual = service.findById(existingDataObject.getId());

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual)
					.isNotNull()
					.isNotEmpty()
					.hasValue(expected);
		}

		@Test
		void givenNonExistentDataObjectId_whenFindById_thenReturnEmpty() {
			//given
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Optional<DataObject> actual = service.findById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual)
					.isNotNull()
					.isEmpty();
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenExistingDataObjects_whenFindAll_thenReturnListOfDataObjects() {
			//given
			List<DataObject> expected = createDataObjectList();
			List<DataObject> dataObjects = createDataObjectList();

			Mockito.when(repository.findAll()).thenReturn(dataObjects);

			//when
			List<DataObject> actual = service.findAll();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.hasSameSizeAs(expected)
					.hasSameElementsAs(expected);
		}

		@Test
		void givenNoDataObjects_whenFindAll_thenReturnEmptyListOfDataObjects() {
			//given
			Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());

			//when
			List<DataObject> actual = service.findAll();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEmpty();
		}
	}

	private DataObject createDataObject(Integer id, String identifier) {
		return DataObject.builder()
				.id(id)
				.identifier(identifier)
				.description("description")
				.system(System.builder().id(1).build())
				.user(user)
				.originalFileName("filename")
				.content(createValidContent())
				.build();
	}

	private DataObject createInvalidDataObject(Integer id, String identifier) {
		return DataObject.builder()
				.id(id)
				.identifier(identifier)
				.description("description")
				.status(DataObjectStatus.ERROR)
				.system(System.builder().id(1).build())
				.user(user)
				.originalFileName("filename")
				.content(createValidContent())
				.build();
	}

	private List<DataObject> createDataObjectList() {
		return List.of(createDataObject(1, "test1"),
				createDataObject(2, "test2"),
				createDataObject(3, "test3"));
	}

	private String createValidContent() {
		return "       01 TRCNVTRX.";
	}

	private DataObjectFile createValidImportDataObjectResult() {
		return DataObjectFile.builder().build();
	}

	private DataObjectFile createInvalidImportDataObjectResult() {
		return DataObjectFile.builder().valid(Boolean.FALSE).build();
	}
	*/
}
