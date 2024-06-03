package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetType;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.AssetTypeService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetTypeDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.AssetTypeMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class AssetTypeServiceImpl implements AssetTypeService {

    private final Logger log = LoggerFactory.getLogger(AssetTypeServiceImpl.class);

    @Inject
    AssetTypeMapper assetTypeMapper;

    @Override
    @Transactional
    public AssetTypeDTO persistOrUpdate(AssetTypeDTO assetTypeDTO) {
        log.debug("Request to save AssetType : {}", assetTypeDTO);
        var assetType = assetTypeMapper.toEntity(assetTypeDTO);
        assetType = AssetType.persistOrUpdate(assetType);
        return assetTypeMapper.toDto(assetType);
    }

    /**
     * Delete the AssetType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete AssetType : {}", id);
        AssetType.findByIdOptional(id).ifPresent(assetType -> {
            assetType.delete();
        });
    }

    /**
     * Get one assetType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<AssetTypeDTO> findOne(Long id) {
        log.debug("Request to get AssetType : {}", id);
        return AssetType.findByIdOptional(id).map(assetType -> assetTypeMapper.toDto((AssetType) assetType));
    }

    /**
     * Get all the assetTypes.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<AssetTypeDTO> findAll(Page page) {
        log.debug("Request to get all AssetTypes");
        return new Paged<>(AssetType.findAll().page(page)).map(assetType -> assetTypeMapper.toDto((AssetType) assetType));
    }
}
