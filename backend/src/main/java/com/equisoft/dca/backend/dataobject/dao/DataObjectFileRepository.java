package com.equisoft.dca.backend.dataobject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equisoft.dca.backend.dataobject.model.DataObjectFile;

@Repository
public interface DataObjectFileRepository extends JpaRepository<DataObjectFile, Integer> {
	List<DataObjectFile> findByDataObjectIdOrderByDateTimeUploadedDesc(Integer dataObjectId);

	@Modifying
	@Transactional
	@Query("delete from DataObjectFile f where f.dataObject.id = :dataObjectId ")
	void deleteByDataObjectId(@Param("dataObjectId") Integer dataObjectId);
}
