package com.company.project.uber.uperApp;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfiguration {

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        DockerImageName image = DockerImageName.parse("postgis/postgis:12-3.0")
                .asCompatibleSubstituteFor("postgres");
        return new PostgreSQLContainer<>(image);
    }
}
