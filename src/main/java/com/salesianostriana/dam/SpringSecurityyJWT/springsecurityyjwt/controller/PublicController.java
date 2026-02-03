package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.dto.ApiResponse;

@RestController
@RequestMapping("/api/public")
public class PublicController {

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
