package org.goros.habit_tracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Gamified Habit Tracker API", version = "v3", description = "API documentation for the Gamified Habit Tracker application"))
@SecurityScheme(scheme = "bearer", type= SecuritySchemeType.HTTP, name = "basicAuth", in = SecuritySchemeIn.HEADER)
public class SrGroup02SpringMiniProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrGroup02SpringMiniProjectApplication.class, args);
    }

}
