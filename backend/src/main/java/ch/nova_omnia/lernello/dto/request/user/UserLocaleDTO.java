package ch.nova_omnia.lernello.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UserLocaleDTO(@NotBlank String locale) {
}
