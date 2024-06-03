package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Locality;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.LocalityService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.LocalityDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.LocalityMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class LocalityServiceImpl implements LocalityService {

    private final Logger log = LoggerFactory.getLogger(LocalityServiceImpl.class);

    @Inject
    LocalityMapper localityMapper;

    @Override
    @Transactional
    public LocalityDTO persistOrUpdate(LocalityDTO localityDTO) {
        log.debug("Request to save Locality : {}", localityDTO);
        var locality = localityMapper.toEntity(localityDTO);
        locality = Locality.persistOrUpdate(locality);
        return localityMapper.toDto(locality);
    }

    /**
     * Delete the Locality by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Locality : {}", id);
        Locality.findByIdOptional(id).ifPresent(locality -> {
            locality.delete();
        });
    }

    /**
     * Get one locality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<LocalityDTO> findOne(Long id) {
        log.debug("Request to get Locality : {}", id);
        return Locality.findByIdOptional(id).map(locality -> localityMapper.toDto((Locality) locality));
    }

    /**
     * Get all the localities.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<LocalityDTO> findAll(Page page) {
        log.debug("Request to get all Localities");
        return new Paged<>(Locality.findAll().page(page)).map(locality -> localityMapper.toDto((Locality) locality));
    }
}
