package ch.nova_omnia.lernello.dto.response.block;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class BlockResDTO {
    
    @NotNull
    private final UUID uuid;
    @NotBlank
    private final String name;
    @Min(0)
    private final int position;

}
