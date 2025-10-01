package com.vibecheck.VibeCheck_Backend;

// Importações do Spring Boot.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Habilita o suporte ao cache
public class VibeCheckBackendApplication {
    public static void main(String[] args) {
        // Inicializa a aplicação Spring Boot.
        SpringApplication.run(VibeCheckBackendApplication.class, args);
    }
}
