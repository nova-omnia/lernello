package ch.nova_omnia.pm4.model.data;

/**
 * AbstractDTO is an abstract class that serves as a Data Transfer Object (DTO) 
 * with common properties and methods for its subclasses.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the DTO.</li>
 *   <li>{@code name} - The name associated with the DTO.</li>
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
public abstract class AbstractDTO {
    private Long id;
    private String name;

    /**
     * Returns the unique identifier for the DTO.
     * 
     * @return the unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the DTO.
     * 
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name associated with the DTO.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name associated with the DTO.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}