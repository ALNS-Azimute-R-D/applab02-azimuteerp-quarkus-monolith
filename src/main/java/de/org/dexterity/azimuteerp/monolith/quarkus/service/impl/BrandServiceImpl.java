package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Brand;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.BrandService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.BrandDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.BrandMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class BrandServiceImpl implements BrandService {

    private final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Inject
    BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandDTO persistOrUpdate(BrandDTO brandDTO) {
        log.debug("Request to save Brand : {}", brandDTO);
        var brand = brandMapper.toEntity(brandDTO);
        brand = Brand.persistOrUpdate(brand);
        return brandMapper.toDto(brand);
    }

    /**
     * Delete the Brand by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Brand : {}", id);
        Brand.findByIdOptional(id).ifPresent(brand -> {
            brand.delete();
        });
    }

    /**
     * Get one brand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<BrandDTO> findOne(Long id) {
        log.debug("Request to get Brand : {}", id);
        return Brand.findByIdOptional(id).map(brand -> brandMapper.toDto((Brand) brand));
    }

    /**
     * Get all the brands.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<BrandDTO> findAll(Page page) {
        log.debug("Request to get all Brands");
        return new Paged<>(Brand.findAll().page(page)).map(brand -> brandMapper.toDto((Brand) brand));
    }
}
