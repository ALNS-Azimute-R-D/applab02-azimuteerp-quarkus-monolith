package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TypeOfPersonDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfPerson}.
 */
public interface TypeOfPersonService {
    /**
     * Save a typeOfPerson.
     *
     * @param typeOfPersonDTO the entity to save.
     * @return the persisted entity.
     */
    TypeOfPersonDTO persistOrUpdate(TypeOfPersonDTO typeOfPersonDTO);

    /**
     * Delete the "id" typeOfPersonDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the typeOfPeople.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<TypeOfPersonDTO> findAll(Page page);

    /**
     * Get the "id" typeOfPersonDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeOfPersonDTO> findOne(Long id);
}
