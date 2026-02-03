package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.model.Role;
import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.model.User;
import com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Inicializando usuarios de prueba...");

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@example.com")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .email("user@example.com")
                    .role(Role.USER)
                    .enabled(true)
                    .build();

            User guest = User.builder()
                    .username("guest")
                    .password(passwordEncoder.encode("guest"))
                    .email("guest@example.com")
                    .role(Role.GUEST)
                    .enabled(true)
                    .build();

            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(guest);

            log.info("✅ Usuarios inicializados correctamente:");
            log.info("   - admin/admin (ADMIN)");
            log.info("   - user/user (USER)");
            log.info("   - guest/guest (GUEST)");
        }
    }
}
