package com.equisoft.dca.backend.configuration.service;

import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.language.model.Language;

public interface ConfigurationService {

	Language DEFAULT_LANGUAGE = Language.ENGLISH;

	Configuration get(String userIdentifier);
}
