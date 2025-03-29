package ch.nova_omnia.lernello.dto.request.block;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UpdateBlockOrderDTO {
    private List<UUID> blockUuidsInOrder;
}