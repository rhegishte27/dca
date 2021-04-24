/*
package com.equisoft.dca.api.dataobject;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataObjectResourceTest {

	@Mock
	private DataObjectService service;

	@Mock
	private DataObjectMapper mapper;

	@Mock
	private LocationMapper locationMapper;

	@Mock
	private AuthenticationFacade authenticationFacade;

	private DataObjectResource dataObjectResource;

	private DataObject dataObject;
	private DataObjectDto dataObjectDto;
	private List<DataObject> dataObjects;
	private List<DataObjectDto> dataObjectDtos;

	@BeforeEach
	void setUp() {
		dataObjectResource = new DataObjectResource(service, mapper, locationMapper, dataObjectFileMapper, dataObjectTypeMapper, authenticationFacade);
		dataObject = createDataObject(1, "identifier");
		dataObjectDto = createDataObjectDto(dataObject);
		dataObjects = createDataObjectList();
		dataObjectDtos = createDataObjectDtoList(dataObjects);
	}

	@Nested
	class Validate {

		@Mock
		private Location location;

		@Mock
		private LocationDto locationDto;

		@Mock
		private List<DataObject> listDataObjectsBeforeValidate;

		@Mock
		private List<DataObjectDto> listDataObjectsDtoBeforeValidate;

		@Mock
		private List<DataObject> listDataObjectsAfterValidate;

		@Mock
		private List<DataObjectDto> listDataObjectsDtoAfterValidate;

		@Test
		void givenListDto_whenValidate_callServiceValidateAndReturnDto() {
			// given
			Mockito.when(mapper.toEntityList(listDataObjectsDtoBeforeValidate)).thenReturn(listDataObjectsBeforeValidate);
			Mockito.when(locationMapper.toEntity(locationDto)).thenReturn(location);
			Mockito.when(service.validateDataObjects(dataObjectTypeMapper.toEntity(containerDto.getType()), location, listDataObjectsBeforeValidate))
								.thenReturn(listDataObjectsAfterValidate);
			Mockito.when(mapper.toDtoList(listDataObjectsAfterValidate)).thenReturn(listDataObjectsDtoAfterValidate);
			DataObjectContainerDto containerDto =
					DataObjectContainerDto.builder().location(locationDto).dataObjectList(listDataObjectsDtoBeforeValidate).build();

			ResponseEntity expected = ResponseEntity.ok().body(listDataObjectsDtoAfterValidate);

			// when
			ResponseEntity<List<DataObjectDto>> actual = dataObjectResource.validate(containerDto);

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class Add {

		@Test
		void givenValidDataObject_whenAddDataObject_thenReturnStatusCodeCreatedAndDataObject() throws IOException {
			//given
			DataObject dataObjectToSaveMapped = DataObject.builder().identifier("identifier").build();
			DataObjectDto dataObjectDtoToSave = createDataObjectDto(dataObjectToSaveMapped);

			DataObject savedDataObject = createDataObject(1, "identifier");
			DataObjectDto savedDataObjectDtoMapped = createDataObjectDto(savedDataObject);
			String userIdentifier = "username";

			DataObjectDto dataObjectExpected = createDataObjectDto(savedDataObject);
			ResponseEntity expected = ResponseEntity.created(URI.create(DataObjectResource.PATH + "/" + dataObject.getId())).body(dataObjectExpected);

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.ofNullable(userIdentifier));
			Mockito.when(mapper.toEntity(ArgumentMatchers.any(DataObjectDto.class))).thenReturn(dataObjectToSaveMapped);
			Mockito.when(service.save(ArgumentMatchers.any(DataObject.class))).thenReturn(savedDataObject);
			Mockito.when(mapper.toDto(ArgumentMatchers.any(DataObject.class))).thenReturn(savedDataObjectDtoMapped);

			//when
			ResponseEntity<DataObjectDto> actual = dataObjectResource.add(dataObjectDtoToSave);

			//then
			Mockito.verify(authenticationFacade, Mockito.times(1)).getUserIdentifier();
			Mockito.verify(mapper, Mockito.times(1)).toEntity(ArgumentMatchers.any(DataObjectDto.class));
			Mockito.verify(service, Mockito.times(1)).save(ArgumentMatchers.any(DataObject.class));
			Mockito.verify(mapper, Mockito.times(1)).toDto(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getHeaders, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getHeaders(), expected.getBody());
		}

		@Test
		void givenNoUserLoggedIn_whenAddDataObject_thenThrowAccessDeniedException() {
			//given
			Class expected = AccessDeniedException.class;

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> dataObjectResource.add(dataObjectDto));

			//then
			Mockito.verify(authenticationFacade, Mockito.times(1)).getUserIdentifier();
			Mockito.verify(mapper, Mockito.times(0)).toEntity(ArgumentMatchers.any(DataObjectDto.class));
			Mockito.verify(service, Mockito.times(0)).save(ArgumentMatchers.any(DataObject.class));
			Mockito.verify(mapper, Mockito.times(0)).toDto(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class Create {
		@Mock
		private List<DataObject> listDataObjectsBeforeValidate;

		@Mock
		private List<DataObjectDto> listDataObjectsDtoBeforeValidate;

		@Mock
		private List<DataObject> listDataObjectsAfterValidate;

		@Mock
		private List<DataObjectDto> listDataObjectsDtoAfterValidate;

		@Test
		void givenListDto_whenCreate_callServiceCreateAndReturnDto() {
			// given
			Mockito.when(mapper.toEntityList(listDataObjectsDtoBeforeValidate)).thenReturn(listDataObjectsBeforeValidate);
			Mockito.when(service.create(listDataObjectsBeforeValidate)).thenReturn(listDataObjectsAfterValidate);
			Mockito.when(mapper.toDtoList(listDataObjectsAfterValidate)).thenReturn(listDataObjectsDtoAfterValidate);

			ResponseEntity expected = ResponseEntity.ok().body(listDataObjectsDtoAfterValidate);

			// when
			ResponseEntity<List<DataObjectDto>> actual = dataObjectResource.create(listDataObjectsDtoBeforeValidate);

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class ImportDataObjects {
		@Mock
		private List<DataObject> listDataObjectsBeforeValidate;

		@Mock
		private List<DataObjectDto> listDataObjectsDtoBeforeValidate;

		@Mock
		private List<DataObject> listDataObjectsAfterValidate;

		@Mock
		private List<DataObjectDto> listDataObjectsDtoAfterValidate;

		@Test
		void givenListDto_whenImportDataObject_callServiceSaveAndReturnDto() {
			// given
			String userIdentifier = "user";
			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.of(userIdentifier));
			Mockito.when(mapper.toEntityList(listDataObjectsDtoBeforeValidate)).thenReturn(listDataObjectsBeforeValidate);
			Mockito.when(service.save(userIdentifier, listDataObjectsBeforeValidate)).thenReturn(listDataObjectsAfterValidate);
			Mockito.when(mapper.toDtoList(listDataObjectsAfterValidate)).thenReturn(listDataObjectsDtoAfterValidate);

			ResponseEntity expected = ResponseEntity.ok().body(listDataObjectsDtoAfterValidate);

			// when
			ResponseEntity<List<DataObjectDto>> actual = dataObjectResource.importDataObjects(listDataObjectsDtoBeforeValidate);

			// then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class Update {

		@Test
		void givenValidDataObject_whenUpdateDataObject_thenReturnStatusCodeOkAndBodyNull() throws IOException {
			//given
			ResponseEntity expected = ResponseEntity.ok().build();
			String userIdentifier = "username";

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.ofNullable(userIdentifier));
			Mockito.when(mapper.toEntity(ArgumentMatchers.any(DataObjectDto.class))).thenReturn(dataObject);
			Mockito.when(service.update(ArgumentMatchers.any(DataObject.class))).thenReturn(dataObject);

			//when
			ResponseEntity<Void> actual = dataObjectResource.update(dataObjectDto.getId(), dataObjectDto);

			//then
			Mockito.verify(authenticationFacade, Mockito.times(1)).getUserIdentifier();
			Mockito.verify(mapper, Mockito.times(1)).toEntity(ArgumentMatchers.any(DataObjectDto.class));
			Mockito.verify(service, Mockito.times(1)).update(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNoUserLoggedIn_whenUpdateDataObject_thenThrowAccessDeniedException() {
			//given
			Class expected = AccessDeniedException.class;

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> dataObjectResource.update(dataObjectDto.getId(), dataObjectDto));

			//then
			Mockito.verify(authenticationFacade, Mockito.times(1)).getUserIdentifier();
			Mockito.verify(mapper, Mockito.times(0)).toEntity(ArgumentMatchers.any(DataObjectDto.class));
			Mockito.verify(service, Mockito.times(0)).update(ArgumentMatchers.any(DataObject.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class Delete {

		@Test
		void givenExistingDataObjectId_whenDeleteDataObject_thenReturnStatusCodeNoContentAndBodyNull() {
			//given
			ResponseEntity expected = ResponseEntity.noContent().build();

			//when
			ResponseEntity<Void> actual = dataObjectResource.delete(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistingDataObjectId_whenFindDataObjectById_thenReturnStatusCodeOkAndBodyWithDataObject() {
			//given
			DataObjectDto dataObjectDtoExpected = createDataObjectDto(dataObject);
			ResponseEntity expected = ResponseEntity.ok().body(dataObjectDtoExpected);

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(dataObject));
			Mockito.when(mapper.toDto(ArgumentMatchers.any(DataObject.class))).thenReturn(dataObjectDto);

			//when
			ResponseEntity<DataObjectDto> actual = dataObjectResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentDataObjectId_whenFindDataObjectById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> dataObjectResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenExistingDataObjects_whenFindAllDataObjects_thenReturnStatusCodeOkAndBodyDataObjectList() {
			//given
			List<DataObjectDto> dataObjectDtosExpected = createDataObjectDtoList(dataObjects);
			ResponseEntity expected = ResponseEntity.ok().body(dataObjectDtosExpected);

			Mockito.when(service.findAll()).thenReturn(dataObjects);
			Mockito.when(mapper.toDtoList(ArgumentMatchers.anyList())).thenReturn(dataObjectDtos);

			//when
			ResponseEntity<List<DataObjectDto>> actual = dataObjectResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNoSystems_whenFindAllSystems_thenReturnStatusCodeOkAndBodyEmpty() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(Collections.emptyList());

			Mockito.when(service.findAll()).thenReturn(Collections.emptyList());

			//when
			ResponseEntity<List<DataObjectDto>> actual = dataObjectResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private DataObject createDataObject(Integer id, String identifier) {
		return DataObject.builder()
				.id(id)
				.identifier(identifier)
				.description("description")
				.system(System.builder().id(1).build())
				.user(User.builder().id(1).build())
				.originalFileName("filename")
				.content(createValidContent())
				.build();
	}

	private DataObjectDto createDataObjectDto(DataObject dataObject) {
		return DataObjectDto.builder()
				.id(dataObject.getId())
				.identifier(dataObject.getIdentifier())
				.description(dataObject.getDescription())
				.systemId(dataObject.getSystem() != null ? dataObject.getSystem().getId() : null)
				.originalFileName(dataObject.getOriginalFileName())
				.content(dataObject.getContent())
				.build();
	}

	private List<DataObject> createDataObjectList() {
		return List.of(createDataObject(1, "test1"),
				createDataObject(2, "test2"),
				createDataObject(3, "test3"));
	}

	private List<DataObjectDto> createDataObjectDtoList(List<DataObject> dataObjects) {
		return dataObjects
				.stream()
				.map(this::createDataObjectDto)
				.collect(Collectors.toList());
	}

	private String createValidContent() {
		return "       01 TRCNVTRX.";
	}
}
*/
