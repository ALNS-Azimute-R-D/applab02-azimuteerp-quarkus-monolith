package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TenantDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Tenant}.
 */
public interface TenantService {
    /**
     * Save a tenant.
     *
     * @param tenantDTO the entity to save.
     * @return the persisted entity.
     */
    TenantDTO persistOrUpdate(TenantDTO tenantDTO);

    /**
     * Delete the "id" tenantDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the tenants.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<TenantDTO> findAll(Page page);

    /**
     * Get the "id" tenantDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TenantDTO> findOne(Long id);
}
