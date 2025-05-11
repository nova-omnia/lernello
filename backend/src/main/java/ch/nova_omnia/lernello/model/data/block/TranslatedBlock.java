package ch.nova_omnia.lernello.model.data.block;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TranslatedBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Block block;

    @Column(nullable = false, length = 5)
    private BlockLanguage Language;

    @Lob
    private String content;

    @Lob
    private String question;

    @Lob
    private String expectedAnswer;

    @ElementCollection
    private List<String> possibleAnswers;

    @ElementCollection
    private List<String> correctAnswers;
}
