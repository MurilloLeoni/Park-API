package com.mleoni.demo_park_api.web.controller;

import com.mleoni.demo_park_api.web.controller.dto.UserCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.UserPasswordDTO;
import com.mleoni.demo_park_api.web.controller.dto.UserResponseDTO;
import com.mleoni.demo_park_api.web.controller.dto.mapper.UserMapper;
import com.mleoni.demo_park_api.entities.User;
import com.mleoni.demo_park_api.services.UserService;
import com.mleoni.demo_park_api.web.controller.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Contains all operations related to the resources for registering, editing and reading a user.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Resource to create a new user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "409", description = "User email already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO createDto) {
        User user = userService.save(UserMapper.toUser(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @Operation(summary = "Find a user by id", description = "Resource to find a user by id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource recovered successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        User user = userService.searchById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(summary = "Update password", description = "Resource to update password.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password updated successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Password does not match.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid or poorly formatted fields",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO userDto) {
        User user = userService.editPassword(id, userDto.getCurrentPassword(), userDto.getNewPassword(), userDto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all users", description = "Resource to list all users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource recovered successfully.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))))
            })
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
