package com.unchk.plateform_suivi_tutorat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Plateforme de Suivi de Tutorat")
                        .version("1.0")
                        .description("Documentation de l'API pour la plateforme de suivi de tutorat"));
    }
}