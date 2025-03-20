package ch.nova_omnia.lernello.model.data.blocks;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("theory")
public class TheoryBlock extends Block {
    //TODO: Implement TheoryBlock
}