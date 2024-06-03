package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PersonDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Person}.
 */
public interface PersonService {
    /**
     * Save a person.
     *
     * @param personDTO the entity to save.
     * @return the persisted entity.
     */
    PersonDTO persistOrUpdate(PersonDTO personDTO);

    /**
     * Delete the "id" personDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the people.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<PersonDTO> findAll(Page page);

    /**
     * Get the "id" personDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonDTO> findOne(Long id);
}
