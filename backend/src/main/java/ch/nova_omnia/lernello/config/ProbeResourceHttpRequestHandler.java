package ch.nova_omnia.lernello.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;

public class ProbeResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    @Override
    protected MediaType getMediaType(@NonNull HttpServletRequest request, @NonNull Resource resource) {
        try (InputStream in = resource.getInputStream()) {
            String probed = detectMimeType(in);
            if (probed != null) {
                return MediaType.parseMediaType(probed);
            }
        } catch (IOException ignored) {
            // fallback to default
        }
        return super.getMediaType(request, resource);
    }

    private static String detectMimeType(InputStream is) throws IOException {
        byte[] header = new byte[16];
        int read;
        if (is.markSupported()) {
            is.mark(header.length);
            read = is.read(header);
            is.reset();
        } else {
            read = is.read(header);
        }
        return detectMimeType(Arrays.copyOf(header, Math.max(read, 0)));
    }

    private static String detectMimeType(byte[] b) {
        int len = b.length;
        // RIFF containers: WAV, AVI, WEBP, AIFF
        if (len >= 12 && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F') {
            String fcc = new String(b, 8, 4);
            switch (fcc) {
                case "WAVE":
                    return "audio/wav";
                case "AVI ":
                    return "video/x-msvideo";
                case "WEBP":
                    return "image/webp";
                case "AIFF":
                    return "audio/aiff";
            }
        }
        // Matroska (MKV/WebM)
        if (len >= 4 && (b[0] & 0xFF) == 0x1A && (b[1] & 0xFF) == 0x45 && (b[2] & 0xFF) == 0xDF && (b[3] & 0xFF) == 0xA3) {
            return "video/x-matroska";
        }
        // ISO Base Media / MP4 / M4A
        if (len >= 12 && new String(b, 4, 4).equals("ftyp")) {
            String brand = new String(b, 8, 4);
            if (brand.startsWith("M4A") || brand.startsWith("M4B")) {
                return "audio/mp4";
            }
            return "video/mp4";
        }
        // PNG
        if (len >= 8 && (b[0] & 0xFF) == 0x89 && b[1] == 0x50 && b[2] == 0x4E && b[3] == 0x47 && b[4] == 0x0D && b[5] == 0x0A && b[6] == 0x1A && b[7] == 0x0A) {
            return "image/png";
        }
        // GIF
        if (len >= 6 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9') && b[5] == 'a') {
            return "image/gif";
        }
        // PDF
        if (len >= 4 && b[0] == '%' && b[1] == 'P' && b[2] == 'D' && b[3] == 'F') {
            return "application/pdf";
        }
        // TIFF
        if (len >= 4 && ((b[0] == 'I' && b[1] == 'I' && b[2] == 0x2A && b[3] == 0x00) || (b[0] == 'M' && b[1] == 'M' && b[2] == 0x00 && b[3] == 0x2A))) {
            return "image/tiff";
        }
        // JPEG
        if (len >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }

        // fallback
        return null;
    }
}
