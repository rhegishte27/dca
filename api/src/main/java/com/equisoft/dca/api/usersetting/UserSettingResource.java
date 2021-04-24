package com.equisoft.dca.api.usersetting;

import java.net.URI;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.usersetting.dto.UserSettingDto;
import com.equisoft.dca.api.usersetting.mapper.UserSettingMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.model.UserSetting;
import com.equisoft.dca.backend.user.service.UserSettingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = UserSettingResource.PATH)
@Tag(name = "UserSetting")
public class UserSettingResource {

	static final String PATH = "/usersettings";

	private final UserSettingService service;

	private final UserSettingMapper userSettingMapper;

	@Inject
	public UserSettingResource(UserSettingService service, UserSettingMapper userSettingMapper) {
		this.service = service;
		this.userSettingMapper = userSettingMapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Save user setting")
	public ResponseEntity<UserSettingDto> save(@RequestBody UserSettingDto userSettingDto) {
		UserSetting userSetting = service.save(userSettingMapper.toEntity(userSettingDto));
		URI location = URI.create(String.format(PATH + "/%d", userSetting.getId()));

		return ResponseEntity.created(location).body(userSettingMapper.toDto(userSetting));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete a user setting")
	public ResponseEntity delete(@PathVariable Integer id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get user setting by identifier")
	public ResponseEntity<UserSettingDto> findById(@PathVariable Integer id) {
		return service.findById(id)
				.map(o -> ResponseEntity.ok(userSettingMapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(UserSetting.class, "id", id.toString()));
	}
}
