package ch.nova_omnia.lernello.config;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Configuration
public class StaticResourceConfig {
    /**
     * Your custom handler that probes files for their real content type.
     */
    @Bean
    ResourceHttpRequestHandler probeResourceHttpRequestHandler() throws MalformedURLException {
        ProbeResourceHttpRequestHandler handler = new ProbeResourceHttpRequestHandler();

        // Point it at your on-disk directory:
        UrlResource filesDir = new UrlResource("file:./data/files/");
        handler.setLocations(List.of(filesDir));

        // Optional: enable caching
        handler.setCacheSeconds(3600);

        return handler;
    }

    /**
     * Map requests for “/files/**” to your handler.
     */
    @Bean
    SimpleUrlHandlerMapping filesHandlerMapping(
                                                ResourceHttpRequestHandler probeResourceHttpRequestHandler) {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        // Make sure it has precedence over Spring Boot’s default static-resource handler
        mapping.setOrder(Ordered.LOWEST_PRECEDENCE - 1);

        Map<String, Object> urlMap = new HashMap<>();
        // any request to /files/** will go through ProbeResourceHttpRequestHandler
        urlMap.put("/files/**", probeResourceHttpRequestHandler);
        mapping.setUrlMap(urlMap);

        return mapping;
    }
}
