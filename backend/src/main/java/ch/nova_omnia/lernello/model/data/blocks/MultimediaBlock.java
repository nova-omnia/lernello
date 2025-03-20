package ch.nova_omnia.lernello.model.data.blocks;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("multimedia")
public class MultimediaBlock extends Block {
    //TODO: Implement MultimediaBlock
}