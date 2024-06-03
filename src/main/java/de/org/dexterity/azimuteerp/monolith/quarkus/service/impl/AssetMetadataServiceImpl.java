package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetMetadata;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.AssetMetadataService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetMetadataDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.AssetMetadataMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class AssetMetadataServiceImpl implements AssetMetadataService {

    private final Logger log = LoggerFactory.getLogger(AssetMetadataServiceImpl.class);

    @Inject
    AssetMetadataMapper assetMetadataMapper;

    @Override
    @Transactional
    public AssetMetadataDTO persistOrUpdate(AssetMetadataDTO assetMetadataDTO) {
        log.debug("Request to save AssetMetadata : {}", assetMetadataDTO);
        var assetMetadata = assetMetadataMapper.toEntity(assetMetadataDTO);
        assetMetadata = AssetMetadata.persistOrUpdate(assetMetadata);
        return assetMetadataMapper.toDto(assetMetadata);
    }

    /**
     * Delete the AssetMetadata by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete AssetMetadata : {}", id);
        AssetMetadata.findByIdOptional(id).ifPresent(assetMetadata -> {
            assetMetadata.delete();
        });
    }

    /**
     * Get one assetMetadata by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<AssetMetadataDTO> findOne(Long id) {
        log.debug("Request to get AssetMetadata : {}", id);
        return AssetMetadata.findByIdOptional(id).map(assetMetadata -> assetMetadataMapper.toDto((AssetMetadata) assetMetadata));
    }

    /**
     * Get all the assetMetadata.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<AssetMetadataDTO> findAll(Page page) {
        log.debug("Request to get all AssetMetadata");
        return new Paged<>(AssetMetadata.findAll().page(page)).map(
            assetMetadata -> assetMetadataMapper.toDto((AssetMetadata) assetMetadata)
        );
    }
}
