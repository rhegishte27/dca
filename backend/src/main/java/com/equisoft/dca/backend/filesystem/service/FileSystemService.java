package com.equisoft.dca.backend.filesystem.service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import com.equisoft.dca.backend.filesystem.model.FileData;

public interface FileSystemService {
	/*
	 * @throws  InvalidPathException
	 *          If the path string cannot be converted
	 */
	Path getPath(String path);

	boolean isPathValid(String path);

	boolean isPathSubFolder(Path pathToValidated, Path parentPath);

	List<FileData> getFilesDirectories(Path path) throws IOException;

	Path createDirectory(Path path) throws IOException;

	FileData createDirectories(Path path) throws IOException;

	void deleteRecursively(Path directory) throws IOException;

	Path createDirectoryInTemporaryDirectory(String directoryPrefix) throws IOException;

	void createFileFromString(String content, String filename, Path directory) throws IOException;

	void createFileFromBytes(byte[] content, String filename, Path directory) throws IOException;

	String extractFilename(String path);

	String readFileToString(Path file) throws IOException;

	byte[] readFileToBytes(Path file) throws IOException;

	boolean fileExists(Path file);

	String getPathFromUrl(URL url);

	boolean isValidDirectory(String path);
}
