package ch.nova_omnia.lernello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatusDTO {
    private String status;
    private String message;

    public JobStatusDTO(String status) {
        this.status = status;
    }
}
