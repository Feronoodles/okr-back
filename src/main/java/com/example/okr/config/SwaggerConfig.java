package com.example.okr.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI SwaggerConfig() {

        Contact contact = new Contact();
        contact.setEmail("ferourlich@gmail.com");
        contact.setName("Fernando");
        contact.setUrl("https://my-awesome-api.com");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Server URL in Local environment");

        Server productionServer = new Server();
        productionServer.setUrl("https://my-awesome-api.com");
        productionServer.setDescription("Server URL in Production environment");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("TASK MANAGER API")
                .contact(contact)
                .version("1.0")
                .description("This API exposes endpoints for users to manage their tasks.")
                .termsOfService("https://my-awesome-api.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, productionServer))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
