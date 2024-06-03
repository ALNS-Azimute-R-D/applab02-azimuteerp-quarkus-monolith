package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.InvoiceDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Invoice}.
 */
public interface InvoiceService {
    /**
     * Save a invoice.
     *
     * @param invoiceDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceDTO persistOrUpdate(InvoiceDTO invoiceDTO);

    /**
     * Delete the "id" invoiceDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the invoices.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<InvoiceDTO> findAll(Page page);

    /**
     * Get the "id" invoiceDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceDTO> findOne(Long id);
}
