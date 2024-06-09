package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Invoice;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.InvoiceService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.InvoiceDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.InvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Inject
    InvoiceMapper invoiceMapper;

    @Override
    @Transactional
    public InvoiceDTO persistOrUpdate(InvoiceDTO invoiceDTO) {
        log.debug("Request to save Invoice : {}", invoiceDTO);
        var invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = Invoice.persistOrUpdate(invoice);
        return invoiceMapper.toDto(invoice);
    }

    /**
     * Delete the Invoice by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        Invoice.findByIdOptional(id).ifPresent(invoice -> {
            invoice.delete();
        });
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<InvoiceDTO> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return Invoice.findByIdOptional(id).map(invoice -> invoiceMapper.toDto((Invoice) invoice));
    }

    /**
     * Get all the invoices.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<InvoiceDTO> findAll(Page page) {
        log.debug("Request to get all Invoices");
        return new Paged<>(Invoice.findAll().page(page)).map(invoice -> invoiceMapper.toDto((Invoice) invoice));
    }
}
