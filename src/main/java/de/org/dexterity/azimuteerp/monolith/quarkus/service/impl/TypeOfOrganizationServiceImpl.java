package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfOrganization;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.TypeOfOrganizationService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TypeOfOrganizationDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.TypeOfOrganizationMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class TypeOfOrganizationServiceImpl implements TypeOfOrganizationService {

    private final Logger log = LoggerFactory.getLogger(TypeOfOrganizationServiceImpl.class);

    @Inject
    TypeOfOrganizationMapper typeOfOrganizationMapper;

    @Override
    @Transactional
    public TypeOfOrganizationDTO persistOrUpdate(TypeOfOrganizationDTO typeOfOrganizationDTO) {
        log.debug("Request to save TypeOfOrganization : {}", typeOfOrganizationDTO);
        var typeOfOrganization = typeOfOrganizationMapper.toEntity(typeOfOrganizationDTO);
        typeOfOrganization = TypeOfOrganization.persistOrUpdate(typeOfOrganization);
        return typeOfOrganizationMapper.toDto(typeOfOrganization);
    }

    /**
     * Delete the TypeOfOrganization by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete TypeOfOrganization : {}", id);
        TypeOfOrganization.findByIdOptional(id).ifPresent(typeOfOrganization -> {
            typeOfOrganization.delete();
        });
    }

    /**
     * Get one typeOfOrganization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<TypeOfOrganizationDTO> findOne(Long id) {
        log.debug("Request to get TypeOfOrganization : {}", id);
        return TypeOfOrganization.findByIdOptional(id).map(
            typeOfOrganization -> typeOfOrganizationMapper.toDto((TypeOfOrganization) typeOfOrganization)
        );
    }

    /**
     * Get all the typeOfOrganizations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<TypeOfOrganizationDTO> findAll(Page page) {
        log.debug("Request to get all TypeOfOrganizations");
        return new Paged<>(TypeOfOrganization.findAll().page(page)).map(
            typeOfOrganization -> typeOfOrganizationMapper.toDto((TypeOfOrganization) typeOfOrganization)
        );
    }
}
