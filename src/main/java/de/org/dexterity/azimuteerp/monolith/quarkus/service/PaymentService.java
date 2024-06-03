package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PaymentDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Payment}.
 */
public interface PaymentService {
    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentDTO persistOrUpdate(PaymentDTO paymentDTO);

    /**
     * Delete the "id" paymentDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the payments.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<PaymentDTO> findAll(Page page);

    /**
     * Get the "id" paymentDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentDTO> findOne(Long id);
}
