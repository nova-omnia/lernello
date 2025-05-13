package ch.nova_omnia.lernello.dto.request.block.update;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;


public record UpdateBlockOrderDTO(
                                  @NotNull List<UUID> blockUuidsInOrder
) {
}