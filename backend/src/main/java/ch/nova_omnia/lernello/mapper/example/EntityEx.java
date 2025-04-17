package ch.nova_omnia.lernello.mapper.example;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class EntityEx {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer quantity;
    private String description;
    private String manufacturer;
    private List<Double> price;
}
