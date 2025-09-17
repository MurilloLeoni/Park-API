package com.mleoni.demo_park_api.controller;

import com.mleoni.demo_park_api.controller.dto.UserCreateDTO;
import com.mleoni.demo_park_api.controller.dto.UserPasswordDTO;
import com.mleoni.demo_park_api.controller.dto.UserResponseDTO;
import com.mleoni.demo_park_api.controller.dto.mapper.UserMapper;
import com.mleoni.demo_park_api.entities.User;
import com.mleoni.demo_park_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserCreateDTO createDto) {
        User user = userService.save(UserMapper.toUser(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        User user = userService.searchById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody UserPasswordDTO userDto) {
        User user = userService.editPassword(id, userDto.getCurrentPassword(), userDto.getNewPassword(), userDto.getConfirmPassword());
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<User> users = userService.searchAll();
        return ResponseEntity.ok(UserMapper.toListDto(users));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
