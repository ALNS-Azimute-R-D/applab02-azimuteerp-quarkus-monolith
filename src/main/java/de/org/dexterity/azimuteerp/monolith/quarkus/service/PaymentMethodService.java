package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PaymentMethodDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.PaymentMethod}.
 */
public interface PaymentMethodService {
    /**
     * Save a paymentMethod.
     *
     * @param paymentMethodDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentMethodDTO persistOrUpdate(PaymentMethodDTO paymentMethodDTO);

    /**
     * Delete the "id" paymentMethodDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the paymentMethods.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<PaymentMethodDTO> findAll(Page page);

    /**
     * Get the "id" paymentMethodDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentMethodDTO> findOne(Long id);
}
