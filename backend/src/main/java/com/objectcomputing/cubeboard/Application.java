package com.objectcomputing.cubeboard;

import com.objectcomputing.cubeboard.versioning.ApiVersion;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "cubedashboardpull",
                version = ApiVersion.V1,
                description = "Cube Dashboard Pull API",
                contact = @Contact(url = "https://objectcomputing.com",
                        name = "Sergio del Amo",
                        email = "delamos@objectcomputing.com")
        )
)
public class Application {

    public static void main(String[] args) {
        long unixTime = System.currentTimeMillis() / 1000L;
        System.out.println(unixTime);
        Micronaut.run(Application.class, args);
    }
}
