package ch.nova_omnia.lernello.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "email_details")
public class EmailDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    @NotBlank
    private String id;

    @NotBlank
    private String recipient;

    @NotBlank
    private String msgBody;

    @NotBlank
    private String subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
