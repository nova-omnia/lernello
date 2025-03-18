package ch.nova_omnia.lernello.model.data;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructors")
@Data
@NoArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    @NotNull
    private UUID uuid;


    @Column(name = "last_name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 40)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @Size(min = 3, max = 40)
    private String firstName;

    public Instructor(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }
}
