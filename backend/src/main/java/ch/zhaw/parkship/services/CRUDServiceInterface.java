package ch.zhaw.parkship.services;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * An interface representing the basic CRUD (Create, Read, Update, Delete) operations for a data entity.
 * @param <T> The type of entity that this service can handle.
 * @param <I> The type of ID that this service uses for entities.
 */
public interface CRUDServiceInterface<T extends Serializable, I> {

    /**
     * Creates a new entity using the provided data.
     * @param data The data to use for creating the entity.
     * @return An Optional containing the created entity, or empty if creation failed.
     */
    public Optional<T> create(T data);

    /**
     * Gets the entity with the specified ID.
     * @param id The ID of the entity to retrieve.
     * @return An Optional containing the retrieved entity, or empty if the entity was not found.
     */
    public Optional<T> getById(I id);

    /**
     * Gets all entities of this type.
     * @return A list containing all entities of this type.
     */
    public List<T> getAll();

    /**
     * Updates an existing entity with the provided data.
     * @param data The data to use for updating the entity.
     * @return An Optional containing the updated entity, or empty if the update failed.
     */
    public Optional<T> update(T data);

    /**
     * Deletes the entity with the specified ID.
     * @param id The ID of the entity to delete.
     * @return An Optional containing the deleted entity, or empty if the entity was not found.
     */
    public Optional<T> deleteById(I id);
    
}
