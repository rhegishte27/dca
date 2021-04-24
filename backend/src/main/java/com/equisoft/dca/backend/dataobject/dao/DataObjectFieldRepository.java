package com.equisoft.dca.backend.dataobject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equisoft.dca.backend.dataobject.model.DataObjectField;

@Repository
public interface DataObjectFieldRepository extends JpaRepository<DataObjectField, Long> {

	@Modifying
	@Transactional
	@Query("delete from DataObjectField f where f.dataObject.id = :dataObjectId ")
	void deleteByDataObjectId(@Param("dataObjectId") Integer dataObjectId);
}
