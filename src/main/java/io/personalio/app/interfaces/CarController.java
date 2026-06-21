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

public class CarController {

    @GetMapping("/")
    public Map<String, Boolean> list() {

        // listing all cars in fleet
    }

    @PutMapping("/:id")
    public void rent() {}

    @PutMapping("/:id")
    public void unrent() {}

    @PostMapping("/create")
    public void create() {
        // creating a car in fleet
    }
}
