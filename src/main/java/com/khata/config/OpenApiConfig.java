package com.khata.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Khata",
                        email = "khata@contactus.com",//To be changed later
                        url = "https://khata.com" //To be changed later
                ),
                description = "Api list of khata", //To be changed later
                title = "Khata",
                version = "1.0"

        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = " PROD ENV",
                        url = "http://localhost:8080" //To be changed
                )
        }
)
public class OpenApiConfig {
}
