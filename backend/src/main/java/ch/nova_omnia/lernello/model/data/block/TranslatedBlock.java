package ch.nova_omnia.lernello.model.data.block;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("TRANSLATED")
@NoArgsConstructor
public class TranslatedBlock extends Block {
    @Column(length = 30)
    private BlockLanguage language;

    @Lob
    private String content;

    @Lob
    private String question;

    @Lob
    @Column(name = "expected_answer")
    private String expectedAnswer;

    @ElementCollection
    private List<String> possibleAnswers;

    @ElementCollection
    private List<String> correctAnswers;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "original_block_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Block originalBlock;

    public TranslatedBlock(BlockLanguage language, String content, Block originalBlock, String name) {
        super(originalBlock.getType(), name, originalBlock.getPosition(), originalBlock.getLearningUnit());
        this.language = language;
        this.content = content;
        this.originalBlock = originalBlock;
    }

    public TranslatedBlock(BlockLanguage language, String question, String expectedAnswer, Block originalBlock, String name) {
        super(originalBlock.getType(), name, originalBlock.getPosition(), originalBlock.getLearningUnit());
        this.language = language;
        this.question = question;
        this.expectedAnswer = expectedAnswer;
        this.originalBlock = originalBlock;
    }
}
