package com.equisoft.dca.backend.dataobject.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.dataobject.dao.DataObjectFileRepository;
import com.equisoft.dca.backend.dataobject.model.DataObjectFile;

@Service
class DataObjectFileServiceImpl implements DataObjectFileService {

	private final DataObjectFileRepository repository;

	@Inject
	DataObjectFileServiceImpl(DataObjectFileRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<DataObjectFile> findLatestResultByDataObjectId(Integer dataObjectId) {
		return repository.findByDataObjectIdOrderByDateTimeUploadedDesc(dataObjectId).stream().findFirst();
	}
}
