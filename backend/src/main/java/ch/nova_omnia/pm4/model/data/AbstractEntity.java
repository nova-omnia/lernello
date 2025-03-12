package ch.nova_omnia.pm4.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * AbstractEntity is an abstract class that serves as a base class for entities
 * with common properties and methods for its subclasses.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the entity.</li>
 *   <li>{@code name} - The name associated with the entity.</li>
 * </ul>
 * 
 * <p>It provides getter and setter methods for these fields:</p>
 * <ul>
 *   <li>{@link #getId()} - Returns the unique identifier.</li>
 *   <li>{@link #setId(Long)} - Sets the unique identifier.</li>
 *   <li>{@link #getName()} - Returns the name.</li>
 *   <li>{@link #setName(String)} - Sets the name.</li>
 * </ul>
 */
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Returns the unique identifier for the entity.
     * 
     * @return the unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the entity.
     * 
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name associated with the entity.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name associated with the entity.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
