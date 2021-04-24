package com.equisoft.dca.backend.dataobject.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.model.DataObjectFile;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.system.model.System;

public interface DataObjectService {

	/*
	DataObject update(@Valid DataObject dataObject);
*/
	DataObject save(@Valid DataObject dataObject);

	List<DataObject> save(String userIdentifier, List<DataObject> dataObjects);

	List<DataObject> create(System system, DataObjectType dataObjectType, List<DataObjectFile> dataObjectFiles);

	void deleteById(Integer id);

	Optional<DataObject> findById(Integer id);

	List<DataObject> findAll();

	List<DataObjectFile> validateDataObjects(DataObjectType dataObjectType, Location location,
			List<DataObjectFile> dataObjectFiles);
}
