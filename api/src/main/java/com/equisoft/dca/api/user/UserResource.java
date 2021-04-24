package com.equisoft.dca.api.user;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.user.dto.UserDto;
import com.equisoft.dca.api.user.mapper.UserMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.service.UserService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = UserResource.PATH)
@Tag(name = "User")
public class UserResource {

	static final String PATH = "/users";

	private final UserService userService;

	private final UserMapper mapper;

	private final AuthenticationFacade authenticationFacade;

	@Inject
	public UserResource(UserService userService, UserMapper mapper, AuthenticationFacade authenticationFacade) {
		this.userService = userService;
		this.mapper = mapper;
		this.authenticationFacade = authenticationFacade;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create an user")
	public ResponseEntity<UserDto> add(@RequestBody UserDto userDto) {
		User user = userService.save(getUserIdentifier(), mapper.toEntity(userDto));
		URI location = URI.create(String.format(PATH + "/%d", user.getId()));

		return ResponseEntity.created(location).body(mapper.toDto(user));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update an user")
	public ResponseEntity update(@PathVariable Integer id, @RequestBody UserDto userDto) {
		userDto.setId(id);
		userService.update(getUserIdentifier(), mapper.toEntity(userDto));
		return ResponseEntity.ok().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete an user")
	public ResponseEntity delete(@PathVariable Integer id) {
		userService.deleteById(getUserIdentifier(), id);
		return ResponseEntity.noContent().build();
	}

	private String getUserIdentifier() {
		return authenticationFacade.getUserIdentifier()
				.orElseThrow(() -> new AccessDeniedException("User must be authenticated to use this feature"));
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all users")
	public ResponseEntity<List<UserDto>> findAll() {
		return ResponseEntity.ok().body(mapper.toDtoList(userService.findAll()));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get an user by identifier")
	public ResponseEntity<UserDto> findById(@PathVariable Integer id) {
		return userService.findById(id)
				.map(o -> ResponseEntity.ok(mapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
	}
}
