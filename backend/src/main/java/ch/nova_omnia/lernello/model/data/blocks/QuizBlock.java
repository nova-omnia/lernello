package ch.nova_omnia.lernello.model.data.blocks;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("quiz")
public class QuizBlock extends Block {
    //TODO: Implement QuizBlock
}