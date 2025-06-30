// Define o pacote onde a classe de configuração está localizada.
package com.vibecheck.VibeCheck_Backend.config;

// Importações necessárias do framework Spring.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A anotação @Configuration indica ao Spring que esta é uma classe de configuração.
 * O Spring irá processar esta classe para definir beans e outras configurações para a aplicação.
 */
@Configuration
public class CorsConfig {

    /**
     * A anotação @Bean diz ao Spring que este método produz um "bean" (um objeto gerenciado pelo Spring).
     * Neste caso, o bean é um WebMvcConfigurer que customiza o comportamento do Spring MVC.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // Retorna uma nova implementação anônima da interface WebMvcConfigurer.
        return new WebMvcConfigurer() {
            /**
             * Sobrescreve o método addCorsMappings para adicionar regras de CORS específicas.
             * @param registry O registro de CORS onde as regras são definidas.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // registry.addMapping("/**") aplica a regra de CORS para TODAS as rotas (endpoints) da API.
                registry.addMapping("/**")
                        // .allowedOrigins("http://localhost:3000") permite que apenas a aplicação rodando
                        // nesta origem (seu frontend em ambiente de desenvolvimento) faça requisições.
                        .allowedOrigins("http://localhost:3000")

                        // .allowedMethods(...) define quais métodos HTTP são permitidos (GET, POST, etc.).
                        // OPTIONS é necessário para requisições de "pre-flight" que o navegador faz.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                        // .allowedHeaders("*") permite que a requisição do frontend inclua quaisquer cabeçalhos HTTP.
                        .allowedHeaders("*")

                        // .allowCredentials(true) permite que o navegador envie credenciais (como cookies ou
                        // cabeçalhos de autorização) junto com a requisição. Essencial para autenticação.
                        .allowCredentials(true);
            }
        };
    }
}