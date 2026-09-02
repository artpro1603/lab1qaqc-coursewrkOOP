package ua.opnu.labwork21.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI airlineOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Система управління авіарейсами")
                .version("1.0.0")
                .description("""
                    REST API для управління авіарейсами, пасажирами, бронюваннями, повітряними суднами та класами обслуговування.
                    
                    Система дозволяє:
                    - Реєструвати та керувати пасажирами (унікальні email та номер паспорта)
                    - Створювати рейси та прив'язувати до них повітряні судна
                    - Бронювати квитки на рейси з перевіркою дублікатів та статусів
                    - Керувати класами обслуговування (економ, бізнес тощо)
                    - Переглядати аналітику: кількість рейсів, пасажирів, активних бронювань
                    
                    Технічний стек: Java 17, Spring Boot 4, Spring Data JPA, PostgreSQL, Hibernate.
                    """)
                .contact(new Contact()
                    .name("Кафедра програмної інженерії, ОПНУ")
                    .email("student@opnu.edu.ua")
                )
            );
    }
}
