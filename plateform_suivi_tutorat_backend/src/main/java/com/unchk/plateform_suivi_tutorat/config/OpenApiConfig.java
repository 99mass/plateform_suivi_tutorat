package com.unchk.plateform_suivi_tutorat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                        .description("Documentation de l'API pour la plateforme de suivi de tutorat. Cette API facilite la gestion et le suivi des séances hebdomadaires effectuées par les tuteurs pour différents modules et groupes d'apprenants. Elle permet également la gestion des utilisateurs, des affectations, et la génération de rapports sur les séances effectuées.")
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("Support Technique")
                                .url("https://example.com/support")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );

    }
}