package com.equisoft.dca.api.directory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.directory.dto.FileDataDto;
import com.equisoft.dca.api.directory.mapper.FileDataMapper;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = DirectoryResource.PATH)
@Tag(name = "Directory")
public class DirectoryResource {

	static final String PATH = "/directories";

	private final FileSystemService fileSystemService;

	private final FileDataMapper mapper;

	@Inject
	public DirectoryResource(FileSystemService fileSystemService, FileDataMapper mapper) {
		this.fileSystemService = fileSystemService;
		this.mapper = mapper;
	}

	@JsonRequestMapping(value = "/**", method = RequestMethod.GET)
	@Operation(summary = "Get all directories")
	public ResponseEntity<List<FileDataDto>> getDirectories(HttpServletRequest request) throws IOException {
		String root = getPathFromRequest(request);
		return ResponseEntity.ok().body(mapper.toDtoList(fileSystemService.getFilesDirectories(fileSystemService.getPath(root))));
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create a directory")
	public ResponseEntity<FileDataDto> createDirectory(@RequestBody FileDataDto fileDataDto) throws IOException {
		Path path = fileSystemService.getPath(fileDataDto.getPath()).resolve(fileDataDto.getName());

		URI location = URI.create(String.format(PATH + "/%s", path.getFileName()));
		return ResponseEntity.created(location)
				.body(mapper.toDto(fileSystemService.createDirectories(path)));
	}

	@JsonRequestMapping(value = "/**", method = RequestMethod.DELETE)
	@Operation(summary = "Delete a directory")
	public ResponseEntity<Void> deleteFile(HttpServletRequest request) throws IOException {
		String path = getPathFromRequest(request);

		fileSystemService.deleteRecursively(fileSystemService.getPath(path));
		return ResponseEntity.noContent().build();
	}

	private String getPathFromRequest(HttpServletRequest request) {
		String uri = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		return uri.split(PATH + "/")[1];
	}
}
