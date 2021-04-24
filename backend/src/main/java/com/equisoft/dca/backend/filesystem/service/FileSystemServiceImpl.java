package com.equisoft.dca.backend.filesystem.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import com.equisoft.dca.backend.filesystem.model.FileData;

@Service
class FileSystemServiceImpl implements FileSystemService {

	private final FileSystem fileSystem;

	@Inject
	FileSystemServiceImpl(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	@Override
	public Path getPath(String path) {
		Objects.requireNonNull(path, "Path cannot be null");
		return fileSystem.getPath(path);
	}

	@Override
	public boolean isPathValid(String path) {
		try {
			getPath(path);
			return true;
		} catch (InvalidPathException e) {
			return false;
		}
	}

	@Override
	public boolean isPathSubFolder(Path pathToValidate, Path parentPath) {
		assertPathNotNull(pathToValidate);
		assertPathNotNull(parentPath);

		return !pathToValidate.equals(parentPath)
				&& pathToValidate.startsWith(parentPath);
	}

	@Override
	public List<FileData> getFilesDirectories(Path path) throws IOException {
		assertPathNotNull(path);

		return Files.walk(path)
				.map(p -> {
					FileData fileData = buildFileDataFromPath(p);
					if (p.equals(path)) {
						fileData.setParentPath(null);
					}
					return fileData;
				}).collect(Collectors.toList());
	}

	@Override
	public Path createDirectory(Path path) throws IOException {
		assertPathNotNull(path);
		return Files.createDirectories(path);
	}

	@Override
	public FileData createDirectories(Path path) throws IOException {
		assertPathNotNull(path);
		Files.createDirectories(path);

		return buildFileDataFromPath(path);
	}

	@Override
	public void deleteRecursively(Path directory) throws IOException {
		assertPathNotNull(directory);
		FileSystemUtils.deleteRecursively(directory);
	}

	@Override
	public Path createDirectoryInTemporaryDirectory(String directoryPrefix) throws IOException {
		return Files.createTempDirectory(directoryPrefix);
	}

	@Override
	public void createFileFromString(String content, String filename, Path directory) throws IOException {
		Files.writeString(getPath(directory.toString() + "/" + filename), content, StandardOpenOption.CREATE_NEW);
	}

	@Override
	public void createFileFromBytes(byte[] content, String filename, Path directory) throws IOException {
		Files.write(getPath(directory.toString() + "/" + filename), content, StandardOpenOption.CREATE_NEW);
	}

	@Override
	public String extractFilename(String path) {
		return Optional.ofNullable(path)
				.map(p -> getPath(p).getFileName().toString())
				.orElse(null);
	}

	@Override
	public String readFileToString(Path file) throws IOException {
		return Files.readString(file);
	}

	@Override
	public byte[] readFileToBytes(Path file) throws IOException {
		return Files.readAllBytes(file);
	}

	public boolean fileExists(Path file) {
		return Files.exists(file);
	}

	@Override
	public String getPathFromUrl(URL url) {
		return new File(url.getFile()).getPath();
	}

	private void assertPathNotNull(Path path) {
		Objects.requireNonNull(path, "Path cannot be null");
	}

	private FileData buildFileDataFromPath(Path path) {
		Path fileName = path.getFileName();
		Path parentPath = path.getParent();
		return FileData.builder()
				.path(path.toString())
				.name(fileName != null ? fileName.toString() : null)
				.isDirectory(Files.isDirectory(path))
				.parentPath(parentPath != null ? parentPath.toString() : null)
				.childrenPathList(getListPathChildrenFromPath(path))
				.content(getContentFromPath(path))
				.build();
	}

	private String getContentFromPath(Path path) {
		try {
			return Files.readString(path);
		} catch (IOException e) {
			return null;
		}
	}

	private List<String> getListPathChildrenFromPath(Path path) {
		try {
			return Files.walk(path, 1)
					.filter(p -> !p.equals(path))
					.map(Path::toString).collect(Collectors.toList());
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public boolean isValidDirectory(String path) {
		return new File(path).isDirectory();
	}
}
