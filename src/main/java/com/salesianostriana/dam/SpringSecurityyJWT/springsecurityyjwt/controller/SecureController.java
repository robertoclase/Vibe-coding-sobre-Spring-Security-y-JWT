package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.controller;

import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secure")
public class SecureController {

    @GetMapping("/data")
    public ResponseEntity<ApiResponse> getSecureData(Authentication authentication) {
        String username = authentication.getName();

        return ResponseEntity.ok(new ApiResponse(
                "Acceso a datos protegidos concedido",
                "Usuario autenticado: " + username,
                true
        ));
    }

    @GetMapping("/user-info")
    public ResponseEntity<ApiResponse> getUserInfo(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();

        return ResponseEntity.ok(new ApiResponse(
                "Información del usuario",
                "Usuario: " + username + ", Roles: " + roles,
                true
        ));
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> adminOnly(Authentication authentication) {
        return ResponseEntity.ok(new ApiResponse(
                "Acceso de administrador",
                "Solo admins aquí - Usuario: " + authentication.getName(),
                true
        ));
    }

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse> hello(Authentication authentication) {
        return ResponseEntity.ok(new ApiResponse(
                "Hola usuario autenticado",
                "Bienvenido " + authentication.getName(),
                true
        ));
    }
}
