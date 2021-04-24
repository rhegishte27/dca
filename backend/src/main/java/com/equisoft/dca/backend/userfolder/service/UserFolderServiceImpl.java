package com.equisoft.dca.backend.userfolder.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.filesystem.service.FileSystemService;

@Service
class UserFolderServiceImpl implements UserFolderService {
	private static final String SOURCE_FOLDER = "usersFolder";

	private final FileSystemService fileSystemService;

	@Inject
	UserFolderServiceImpl(FileSystemService fileSystemService) {
		this.fileSystemService = fileSystemService;
	}

	@Override
	public void createUserFolder(String identifier) {
		assertUserIdentifierNotNull(identifier);
		try {
			fileSystemService.createDirectories(getPathFolderUser(identifier));
		} catch (IOException e) {
			throw new RuntimeException("Cannot create user's folder", e);
		}
	}

	@Override
	public void deleteUserFolder(String identifier) {
		assertUserIdentifierNotNull(identifier);
		try {
			fileSystemService.deleteRecursively(getPathFolderUser(identifier));
		} catch (IOException e) {
			throw new RuntimeException("Cannot delete user's folder", e);
		}
	}

	private void assertUserIdentifierNotNull(String identifier) {
		Objects.requireNonNull(identifier, "User identifier cannot be null");
	}

	private Path getPathFolderUser(String username) {
		return fileSystemService.getPath(SOURCE_FOLDER).resolve(username);
	}
}
