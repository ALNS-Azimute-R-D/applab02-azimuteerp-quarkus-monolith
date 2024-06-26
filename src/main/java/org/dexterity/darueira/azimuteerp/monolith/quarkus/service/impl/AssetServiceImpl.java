package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Asset;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.AssetService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.AssetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class AssetServiceImpl implements AssetService {

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    @Inject
    AssetMapper assetMapper;

    @Override
    @Transactional
    public AssetDTO persistOrUpdate(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        var asset = assetMapper.toEntity(assetDTO);
        asset = Asset.persistOrUpdate(asset);
        return assetMapper.toDto(asset);
    }

    /**
     * Delete the Asset by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Asset : {}", id);
        Asset.findByIdOptional(id).ifPresent(asset -> {
            asset.delete();
        });
    }

    /**
     * Get one asset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<AssetDTO> findOne(Long id) {
        log.debug("Request to get Asset : {}", id);
        return Asset.findOneWithEagerRelationships(id).map(asset -> assetMapper.toDto((Asset) asset));
    }

    @Override
    public List<AssetDTO> findAllWhereAssetMetadataIsNull() {
        return List.of();
    }

    /**
     * Get all the assets.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<AssetDTO> findAll(Page page) {
        log.debug("Request to get all Assets");
        return new Paged<>(Asset.findAll().page(page)).map(asset -> assetMapper.toDto((Asset) asset));
    }

    /**
     * Get all the assets with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetDTO> findAllWithEagerRelationships(Page page) {
        var assets = Asset.findAllWithEagerRelationships().page(page).list();
        var totalCount = Asset.findAll().count();
        var pageCount = Asset.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, assets).map(asset -> assetMapper.toDto((Asset) asset));
    }
}
