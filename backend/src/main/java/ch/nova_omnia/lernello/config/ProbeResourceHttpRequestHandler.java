package ch.nova_omnia.lernello.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;

public class ProbeResourceHttpRequestHandler extends ResourceHttpRequestHandler {
    @Override
    protected MediaType getMediaType(@NonNull HttpServletRequest request, @NonNull Resource resource) {
        try {
            Path file = resource.getFile().toPath();
            String probed = Files.probeContentType(file);
            if (probed == null) {
                // fallback to URLConnection’s sniffing
                try (InputStream in = resource.getInputStream()) {
                    byte[] magicBytes = new byte[4];
                    if (in.read(magicBytes) == 4) {
                        String magic = new String(magicBytes);
                        if ("%PDF".equals(magic)) {
                            probed = "application/pdf";
                        }
                    }
                    if (probed == null) {
                        probed = URLConnection.guessContentTypeFromStream(in);
                    }
                    System.out.println("Probed: " + probed);
                } catch (Exception e) {
                    /* ignore */ }
            }
            if (probed != null) {
                return MediaType.parseMediaType(probed);
            }
        } catch (IOException ignored) {
            // fallback to default
        }
        // Spring’s default: based on file name / extension
        return super.getMediaType(request, resource);
    }
}
