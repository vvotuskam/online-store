package com.halyk.onlinestore.controller;

import com.halyk.onlinestore.dto.ErrorResponse;
import com.halyk.onlinestore.dto.auth.request.AuthRequest;
import com.halyk.onlinestore.dto.auth.request.RefreshRequest;
import com.halyk.onlinestore.dto.auth.response.AuthResponse;
import com.halyk.onlinestore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authorization", description = "Authorization and token refresh")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "In case of successful authorization, pair of access and refresh tokens are returned",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = """
                            If fields 'username' or 'password' are blank, then corresponding response
                            with invalid field messages is returned
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = """
                            If an incorrect username or an incorrect password is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "In case of successful refresh, pair of access and refresh tokens are returned",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = """
                            If field 'refreshToken' is blank, then corresponding response
                            with invalid field messages is returned
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),

            @ApiResponse(responseCode = "401",
                    description = """
                            If an invalid refreshToken is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
