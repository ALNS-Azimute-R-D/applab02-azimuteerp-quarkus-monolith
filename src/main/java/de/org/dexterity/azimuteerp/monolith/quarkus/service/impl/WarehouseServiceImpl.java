package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Warehouse;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.WarehouseService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.WarehouseDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.WarehouseMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final Logger log = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    @Inject
    WarehouseMapper warehouseMapper;

    @Override
    @Transactional
    public WarehouseDTO persistOrUpdate(WarehouseDTO warehouseDTO) {
        log.debug("Request to save Warehouse : {}", warehouseDTO);
        var warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse = Warehouse.persistOrUpdate(warehouse);
        return warehouseMapper.toDto(warehouse);
    }

    /**
     * Delete the Warehouse by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Warehouse : {}", id);
        Warehouse.findByIdOptional(id).ifPresent(warehouse -> {
            warehouse.delete();
        });
    }

    /**
     * Get one warehouse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<WarehouseDTO> findOne(Long id) {
        log.debug("Request to get Warehouse : {}", id);
        return Warehouse.findByIdOptional(id).map(warehouse -> warehouseMapper.toDto((Warehouse) warehouse));
    }

    /**
     * Get all the warehouses.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<WarehouseDTO> findAll(Page page) {
        log.debug("Request to get all Warehouses");
        return new Paged<>(Warehouse.findAll().page(page)).map(warehouse -> warehouseMapper.toDto((Warehouse) warehouse));
    }
}
