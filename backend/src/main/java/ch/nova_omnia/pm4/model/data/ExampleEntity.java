package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sosp_model_sys")
public class ExampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "example_column")
    private String exampleColumn;

    public ExampleEntity() {
    }

    public ExampleEntity(String exampleColumn) {
        this.exampleColumn = exampleColumn;
    }
}
