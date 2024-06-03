package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.StockLevel;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.StockLevelService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.StockLevelDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.StockLevelMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class StockLevelServiceImpl implements StockLevelService {

    private final Logger log = LoggerFactory.getLogger(StockLevelServiceImpl.class);

    @Inject
    StockLevelMapper stockLevelMapper;

    @Override
    @Transactional
    public StockLevelDTO persistOrUpdate(StockLevelDTO stockLevelDTO) {
        log.debug("Request to save StockLevel : {}", stockLevelDTO);
        var stockLevel = stockLevelMapper.toEntity(stockLevelDTO);
        stockLevel = StockLevel.persistOrUpdate(stockLevel);
        return stockLevelMapper.toDto(stockLevel);
    }

    /**
     * Delete the StockLevel by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete StockLevel : {}", id);
        StockLevel.findByIdOptional(id).ifPresent(stockLevel -> {
            stockLevel.delete();
        });
    }

    /**
     * Get one stockLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<StockLevelDTO> findOne(Long id) {
        log.debug("Request to get StockLevel : {}", id);
        return StockLevel.findByIdOptional(id).map(stockLevel -> stockLevelMapper.toDto((StockLevel) stockLevel));
    }

    /**
     * Get all the stockLevels.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<StockLevelDTO> findAll(Page page) {
        log.debug("Request to get all StockLevels");
        return new Paged<>(StockLevel.findAll().page(page)).map(stockLevel -> stockLevelMapper.toDto((StockLevel) stockLevel));
    }
}
