package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Supplier;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.SupplierService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.SupplierDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.SupplierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Inject
    SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierDTO persistOrUpdate(SupplierDTO supplierDTO) {
        log.debug("Request to save Supplier : {}", supplierDTO);
        var supplier = supplierMapper.toEntity(supplierDTO);
        supplier = Supplier.persistOrUpdate(supplier);
        return supplierMapper.toDto(supplier);
    }

    /**
     * Delete the Supplier by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        Supplier.findByIdOptional(id).ifPresent(supplier -> {
            supplier.delete();
        });
    }

    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<SupplierDTO> findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        return Supplier.findOneWithEagerRelationships(id).map(supplier -> supplierMapper.toDto((Supplier) supplier));
    }

    /**
     * Get all the suppliers.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<SupplierDTO> findAll(Page page) {
        log.debug("Request to get all Suppliers");
        return new Paged<>(Supplier.findAll().page(page)).map(supplier -> supplierMapper.toDto((Supplier) supplier));
    }

    /**
     * Get all the suppliers with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<SupplierDTO> findAllWithEagerRelationships(Page page) {
        var suppliers = Supplier.findAllWithEagerRelationships().page(page).list();
        var totalCount = Supplier.findAll().count();
        var pageCount = Supplier.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, suppliers).map(
            supplier -> supplierMapper.toDto((Supplier) supplier)
        );
    }
}
