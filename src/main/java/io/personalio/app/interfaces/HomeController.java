package io.personalio.app.interfaces;

import io.personalio.app.domain.User;
import io.personalio.app.domain.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class HomeController {

	private final UserRepository users;
	private final PasswordEncoder passwordEncoder;

	public HomeController(UserRepository users, PasswordEncoder passwordEncoder) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/")
	public Map<String, String> root() {
		return Map.of(
				"name", "Personalio API",
				"status", "ok");
	}

	record CreateUserRequest(
			@NotBlank @Email String email,
			@NotBlank @Size(min = 8) String password) {
	}

	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> createUser(@Valid @RequestBody CreateUserRequest request) {
		if (users.existsByEmail(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}
		User user = users.save(new User(request.email(), passwordEncoder.encode(request.password())));
		return Map.of("id", user.getId(), "email", user.getEmail());
	}

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

}
