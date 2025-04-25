package ch.nova_omnia.lernello.config;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration
public class JacksonConfig {
    @Bean
    Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.ALWAYS).modulesToInstall(new JsonNullableModule());
    }
}
