package gpse.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ExampleApplication.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "SpringFacetCodeInspection"})
@EnableJpaRepositories
@EnableTransactionManagement
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ExampleApplication {
    public static void main(final String... args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}

