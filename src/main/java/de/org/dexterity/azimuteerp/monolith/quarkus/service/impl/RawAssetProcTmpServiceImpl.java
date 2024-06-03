package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmp;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.RawAssetProcTmpService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.RawAssetProcTmpDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.RawAssetProcTmpMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class RawAssetProcTmpServiceImpl implements RawAssetProcTmpService {

    private final Logger log = LoggerFactory.getLogger(RawAssetProcTmpServiceImpl.class);

    @Inject
    RawAssetProcTmpMapper rawAssetProcTmpMapper;

    @Override
    @Transactional
    public RawAssetProcTmpDTO persistOrUpdate(RawAssetProcTmpDTO rawAssetProcTmpDTO) {
        log.debug("Request to save RawAssetProcTmp : {}", rawAssetProcTmpDTO);
        var rawAssetProcTmp = rawAssetProcTmpMapper.toEntity(rawAssetProcTmpDTO);
        rawAssetProcTmp = RawAssetProcTmp.persistOrUpdate(rawAssetProcTmp);
        return rawAssetProcTmpMapper.toDto(rawAssetProcTmp);
    }

    /**
     * Delete the RawAssetProcTmp by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete RawAssetProcTmp : {}", id);
        RawAssetProcTmp.findByIdOptional(id).ifPresent(rawAssetProcTmp -> {
            rawAssetProcTmp.delete();
        });
    }

    /**
     * Get one rawAssetProcTmp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<RawAssetProcTmpDTO> findOne(Long id) {
        log.debug("Request to get RawAssetProcTmp : {}", id);
        return RawAssetProcTmp.findByIdOptional(id).map(rawAssetProcTmp -> rawAssetProcTmpMapper.toDto((RawAssetProcTmp) rawAssetProcTmp));
    }

    /**
     * Get all the rawAssetProcTmps.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<RawAssetProcTmpDTO> findAll(Page page) {
        log.debug("Request to get all RawAssetProcTmps");
        return new Paged<>(RawAssetProcTmp.findAll().page(page)).map(
            rawAssetProcTmp -> rawAssetProcTmpMapper.toDto((RawAssetProcTmp) rawAssetProcTmp)
        );
    }
}
