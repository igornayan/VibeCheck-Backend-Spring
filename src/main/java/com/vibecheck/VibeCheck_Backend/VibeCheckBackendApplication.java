// Define o pacote raiz do projeto.
package com.vibecheck.VibeCheck_Backend;

// Importações do Spring Boot.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class VibeCheckBackendApplication {
	public static void main(String[] args) {
		// Esta linha única inicializa e executa toda a aplicação Spring Boot.
		// Ela cria o contexto da aplicação, realiza o scan de componentes e inicia o servidor web embutido.
		SpringApplication.run(VibeCheckBackendApplication.class, args);
	}

}