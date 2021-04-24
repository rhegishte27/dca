package com.equisoft.dca.backend.dataobject.service;

import java.util.Optional;

import com.equisoft.dca.backend.dataobject.model.DataObjectFile;

public interface DataObjectFileService {
	Optional<DataObjectFile> findLatestResultByDataObjectId(Integer dataObjectId);
}
