package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.controller;

import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de rutas públicas
 *
 * Estos endpoints están disponibles sin autenticación JWT
 * GET /api/public/** - Accesible sin autenticación
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    /**
     * Endpoint público - Accesible sin autenticación
     */
    @GetMapping("/hello")
    public ResponseEntity<ApiResponse> publicEndpoint() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("¡Hola! Este es un endpoint público")
                        .data("Acceso permitido sin autenticación")
                        .success(true)
                        .build()
        );
    }

    /**
     * Información sobre la API
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> apiInfo() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Información de la API")
                        .data("API Spring Security + JWT - Acceso para todos")
                        .success(true)
                        .build()
        );
    }
}
