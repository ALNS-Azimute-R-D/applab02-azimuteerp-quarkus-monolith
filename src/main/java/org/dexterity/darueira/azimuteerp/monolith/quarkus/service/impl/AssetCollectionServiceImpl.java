package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollection;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.AssetCollectionService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetCollectionDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.AssetCollectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class AssetCollectionServiceImpl implements AssetCollectionService {

    private final Logger log = LoggerFactory.getLogger(AssetCollectionServiceImpl.class);

    @Inject
    AssetCollectionMapper assetCollectionMapper;

    @Override
    @Transactional
    public AssetCollectionDTO persistOrUpdate(AssetCollectionDTO assetCollectionDTO) {
        log.debug("Request to save AssetCollection : {}", assetCollectionDTO);
        var assetCollection = assetCollectionMapper.toEntity(assetCollectionDTO);
        assetCollection = AssetCollection.persistOrUpdate(assetCollection);
        return assetCollectionMapper.toDto(assetCollection);
    }

    /**
     * Delete the AssetCollection by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete AssetCollection : {}", id);
        AssetCollection.findByIdOptional(id).ifPresent(assetCollection -> {
            assetCollection.delete();
        });
    }

    /**
     * Get one assetCollection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<AssetCollectionDTO> findOne(Long id) {
        log.debug("Request to get AssetCollection : {}", id);
        return AssetCollection.findOneWithEagerRelationships(id).map(
            assetCollection -> assetCollectionMapper.toDto((AssetCollection) assetCollection)
        );
    }

    /**
     * Get all the assetCollections.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<AssetCollectionDTO> findAll(Page page) {
        log.debug("Request to get all AssetCollections");
        return new Paged<>(AssetCollection.findAll().page(page)).map(
            assetCollection -> assetCollectionMapper.toDto((AssetCollection) assetCollection)
        );
    }

    /**
     * Get all the assetCollections with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetCollectionDTO> findAllWithEagerRelationships(Page page) {
        var assetCollections = AssetCollection.findAllWithEagerRelationships().page(page).list();
        var totalCount = AssetCollection.findAll().count();
        var pageCount = AssetCollection.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, assetCollections).map(
            assetCollection -> assetCollectionMapper.toDto((AssetCollection) assetCollection)
        );
    }
}
