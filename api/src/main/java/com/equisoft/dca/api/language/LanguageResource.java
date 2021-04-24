package com.equisoft.dca.api.language;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.language.mapper.LanguageMapper;
import com.equisoft.dca.backend.language.model.Language;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(LanguageResource.PATH)
@Tag(name = "Language")
public class LanguageResource {

	static final String PATH = "/languages";

	private final LanguageMapper languageMapper;

	@Inject
	public LanguageResource(LanguageMapper languageMapper) {
		this.languageMapper = languageMapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all language")
	public ResponseEntity<List<LanguageDto>> findAll() {
		return ResponseEntity.ok(languageMapper.toDtoList(Arrays.asList(Language.values())));
	}
}
