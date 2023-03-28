package ch.zhaw.parkship.parking.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * An interface that provides basic CRUD (Create, Read, Update, Delete) operations for a given entity.
 *
 * @param <T> The type of entity managed by this service. Must implement the Serializable interface.
 * @param <I> The type of key used to identify the entity.
 */
public interface CRUDServiceInterface<T extends Serializable, I> {
    
    /**
     * Creates a new entity with the provided data.
     *
     * @param data The data from which to create the new entity.
     * @return The newly created entity.
     */
    public T create(T data);

    /**
     * Retrieves all entities managed by this service.
     *
     * @return A list of all entities.
     */
    public List<T> readAll();

    /**
     * Retrieves the entity with the given ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return An Optional containing the entity with the given ID, or an empty Optional if no entity was found.
     */
    public Optional<T> getById(I id);

    /**
     * Updates the entity with the given ID with the provided data.
     *
     * @param id The ID of the entity to update.
     * @param data The new data for the entity.
     * @return The updated entity.
     */
    public T update(I id, T data);

    /**
     * Deletes the entity with the given ID.
     *
     * @param id The ID of the entity to delete.
     */
    public void delete(I id);
}
