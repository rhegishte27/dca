package com.equisoft.dca.backend.dataobject.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.equisoft.dca.backend.dataobject.model.DataObject;

public interface DataObjectRepository extends JpaRepository<DataObject, Integer> {

	Optional<DataObject> findByIdentifierAndSystemId(String identifier, Integer systemId);

	@Transactional
	@Modifying
	@Query(value = "update dataobjects set status = :status where pk_dataobjects = :id", nativeQuery = true)
	int updateDataObjectStatus(@Param("id") Integer id, @Param("status") Integer status);

	@Modifying
	@Query(value = "select * from dataobjects as d join dataobjectfiles as f on d.pk_dataobjects = f.fk_dataobjects where d.fk_systems = :systemid and f"
			+ ".originalfilename = :originalfilename order by f.datetimeuploaded desc",
			nativeQuery = true)
	List<DataObject> findBySystemIdAndDataObjectFilesOriginalFileNameOrderByDataTimeUploadedDesc(@Param("systemid") Integer systemId,
			@Param("originalfilename") String originalFileName);
}
