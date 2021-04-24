package com.equisoft.dca.backend.dataobject.service;

import java.util.List;

import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.model.DataObjectField;

public interface DataObjectFieldService {

	List<DataObjectField> save(DataObject dataObject);

	void deleteByDataObjectId(Integer dataObjectId);
}
