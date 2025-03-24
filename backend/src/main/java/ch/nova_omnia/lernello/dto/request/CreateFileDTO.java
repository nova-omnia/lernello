package ch.nova_omnia.lernello.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public record CreateFileDTO(@NotNull @Size(min = 1, max = 255) String name, MultipartFile file) {
}
