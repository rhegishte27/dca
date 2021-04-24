package com.equisoft.dca.api.setting;

import java.net.URI;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.setting.dto.SettingDto;
import com.equisoft.dca.api.setting.mapper.SettingMapper;
import com.equisoft.dca.backend.setting.model.Setting;
import com.equisoft.dca.backend.setting.service.SettingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = SettingResource.PATH)
@Tag(name = "Setting")
public class SettingResource {

	static final String PATH = "/settings";

	private final SettingService settingService;

	private final SettingMapper mapper;

	@Inject
	public SettingResource(SettingService settingService, SettingMapper mapper) {
		this.settingService = settingService;
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create setting")
	public ResponseEntity<SettingDto> add(@RequestBody SettingDto settingDto) {
		Setting setting = settingService.save(mapper.toEntity(settingDto));

		URI location = URI.create(String.format(PATH + "/%d", setting.getId()));
		return ResponseEntity.created(location).body(mapper.toDto(setting));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update setting")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody SettingDto settingDto) {
		settingDto.setId(id);
		settingService.update(mapper.toEntity(settingDto));
		return ResponseEntity.ok().build();
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get setting")
	public ResponseEntity<SettingDto> get() {
		Setting setting = settingService.get().orElse(Setting.builder().build());
		return ResponseEntity.ok().body(mapper.toDto(setting));
	}
}
