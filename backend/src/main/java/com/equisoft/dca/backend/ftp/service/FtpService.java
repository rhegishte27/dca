package com.equisoft.dca.backend.ftp.service;

import java.util.List;
import java.util.Map;

import com.equisoft.dca.backend.filesystem.model.FileData;
import com.equisoft.dca.backend.location.model.Location;

public interface FtpService {

	List<FileData> listFiles(Location location);

	Map<String, String> downloadFiles(Location location, List<String> files);
}
