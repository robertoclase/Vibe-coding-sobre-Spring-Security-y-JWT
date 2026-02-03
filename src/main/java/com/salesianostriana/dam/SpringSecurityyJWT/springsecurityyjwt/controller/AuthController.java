package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.controller;

import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.dto.ApiResponse;
import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.dto.AuthResponse;
import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.dto.LoginRequest;
import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de Autenticación
 *
 * Endpoints:
 * - POST /api/auth/login - Autentica usuario y devuelve token JWT
 * - POST /api/auth/validate - Valida un token JWT
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Endpoint de login
     * POST /api/auth/login
     *
     * Credenciales disponibles:
     * - admin / admin (rol ADMIN)
     * - user / user (rol USER)
     * - guest / guest (rol GUEST)
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());

        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Obtener UserDetails del Authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generar token JWT
            String token = jwtService.generateToken(userDetails);
            long expiresIn = jwtService.getExpirationInSeconds();

            // Crear respuesta
            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .expiresIn(expiresIn)
                    .username(userDetails.getUsername())
                    .build();

            log.info("Login exitoso para usuario: {}", request.getUsername());

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Login exitoso")
                            .data(authResponse)
                            .success(true)
                            .build()
            );

        } catch (BadCredentialsException e) {
            log.warn("Credenciales inválidas para usuario: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .message("Credenciales inválidas")
                            .success(false)
                            .build()
                    );
        } catch (Exception e) {
            log.error("Error durante el login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Error en la autenticación")
                            .success(false)
                            .build()
                    );
        }
    }

    /**
     * Endpoint para validar un token JWT
     * POST /api/auth/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validateToken(@RequestParam String token) {
        try {
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                return ResponseEntity.ok(
                        ApiResponse.builder()
                                .message("Token válido")
                                .data("Usuario: " + username)
                                .success(true)
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .message("Token inválido o expirado")
                                .success(false)
                                .build()
                        );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .message("Error validando token: " + e.getMessage())
                            .success(false)
                            .build()
                    );
        }
    }
}
