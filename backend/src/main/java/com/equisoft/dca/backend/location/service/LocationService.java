package com.equisoft.dca.backend.location.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.equisoft.dca.backend.filesystem.model.FileData;
import com.equisoft.dca.backend.location.model.Location;

public interface LocationService {

	Optional<Location> findById(Integer id);

	List<Location> findAll();

	Location save(@Valid Location location);

	Location update(@Valid Location location);

	void deleteById(Integer id);

	List<FileData> getFiles(Integer id);
}
