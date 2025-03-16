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

@Entity
@Table(name = "instructors")
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    @NotNull
    private UUID uuid;


    @Column(name = "last_name", nullable = false)
    @NotNull
    @NotBlank
    private String lastName;

    @Column(name = "first_name", nullable = false)
    @NotNull
    @NotBlank
    private String firstName;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull @NotBlank String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull @NotBlank String firstName) {
        this.firstName = firstName;
    }

}
