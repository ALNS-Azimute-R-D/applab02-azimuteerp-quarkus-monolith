package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PaymentGatewayDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGateway}.
 */
public interface PaymentGatewayService {
    /**
     * Save a paymentGateway.
     *
     * @param paymentGatewayDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentGatewayDTO persistOrUpdate(PaymentGatewayDTO paymentGatewayDTO);

    /**
     * Delete the "id" paymentGatewayDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the paymentGateways.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<PaymentGatewayDTO> findAll(Page page);

    /**
     * Get the "id" paymentGatewayDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentGatewayDTO> findOne(Long id);
}
