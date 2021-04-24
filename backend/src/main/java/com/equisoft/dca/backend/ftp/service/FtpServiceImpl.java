package com.equisoft.dca.backend.ftp.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.filesystem.model.FileData;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.ftp.exception.FtpConnectionFailureException;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;

@Service
class FtpServiceImpl implements FtpService {

	private static final String DOWNLOAD_DIRECTORY_PREFIX = "ftp_downloads_";

	private final FileSystemService fileSystemService;

	@Inject
	FtpServiceImpl(FileSystemService fileSystemService) {
		this.fileSystemService = fileSystemService;
	}

	@Override
	public List<FileData> listFiles(Location location) {
		String rootPath = location.getPath();
		List<FileData> files;
		FTPClient ftpClient = open(location);
		try {
			files = Arrays.stream(ftpClient.listFiles(rootPath))
					.filter(FTPFile::isFile)
					.map(f -> FileData.builder()
							.name(f.getName())
							.isDirectory(f.isDirectory())
							.path(getFtpWorkingDirectory(ftpClient) + f.getName())
							.parentPath(rootPath)
							.build())
					.collect(Collectors.toList());
			ftpClient.disconnect();
		} catch (IOException e) {
			throw new FtpConnectionFailureException(e.getMessage());
		}

		files.add(buildRootFileData(rootPath, files));
		return files;
	}

	/**
	 *
	 * @param location FTP server where the files will be downloaded from
	 * @param files List of files to download
	 * @return Map containing the filename as key and its contents as value
	 */
	@Override
	public Map<String, String> downloadFiles(Location location, List<String> files) {
		Objects.requireNonNull(location, "Location cannot be null");

		Map<String, String> result = new HashMap<>();
		FTPClient ftpClient = open(location);
		try {
			Path downloadDirectory = createDownloadDirectory();
			ftpClient.changeWorkingDirectory(location.getPath());
			List<FTPFile> ftpFiles = Arrays.stream(ftpClient.listFiles())
					.filter(FTPFile::isFile)
					.collect(Collectors.toList());
			for (FTPFile ftpFile : ftpFiles) {
				if (files.contains(ftpFile.getName())) {
					String fileContent = download(ftpClient, ftpFile, downloadDirectory);
					result.put(ftpFile.getName(), fileContent);
				}
			}

			ftpClient.disconnect();
			fileSystemService.deleteRecursively(downloadDirectory);
		} catch (IOException e) {
			throw new FtpConnectionFailureException(e.getMessage());
		}
		return result;
	}

	private String download(FTPClient ftpClient, FTPFile ftpFile, Path downloadDirectory) {
		String fileName = downloadDirectory.resolve(ftpFile.getName()).toString();
		try (FileOutputStream out = new FileOutputStream(fileName)) {
			if (ftpClient.retrieveFile(ftpFile.getName(), out)) {
				return fileSystemService.readFileToString(fileSystemService.getPath(fileName));
			}
		} catch (IOException e) {
			throw new FtpConnectionFailureException(e.getMessage());
		}
		return null;
	}

	private Path createDownloadDirectory() throws IOException {
		return fileSystemService.createDirectoryInTemporaryDirectory(DOWNLOAD_DIRECTORY_PREFIX);
	}

	private String getFtpWorkingDirectory(FTPClient ftpClient) {
		try {
			return ftpClient.printWorkingDirectory();
		} catch (IOException e) {
			return "";
		}
	}

	private FileData buildRootFileData(String path, List<FileData> listChildren) {
		return FileData.builder()
				.path(path)
				.name(path)
				.isDirectory(true)
				.childrenPathList(listChildren.stream().map(FileData::getPath).collect(Collectors.toList()))
				.build();
	}

	private FTPClient open(Location location) {
		Objects.requireNonNull(location, "Location cannot be null");
		if (!LocationType.FTP.equals(location.getLocationType())) {
			throw new IllegalArgumentException("Location type must be FTP");
		}

		final FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(location.getServerName());

			final int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				final String replyString = ftpClient.getReplyString();
				ftpClient.disconnect();
				throw new FtpConnectionFailureException(replyString);
			}

			if (!ftpClient.login(location.getUserName(), location.getPassword())) {
				final String replyString = ftpClient.getReplyString();
				ftpClient.disconnect();
				throw new FtpConnectionFailureException(replyString);
			}
			ftpClient.setBufferSize(1024 * 1024);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setAutodetectUTF8(true);
			ftpClient.enterLocalPassiveMode();

			return ftpClient;
		} catch (IOException e) {
			throw new FtpConnectionFailureException(e.getMessage());
		}
	}
}
