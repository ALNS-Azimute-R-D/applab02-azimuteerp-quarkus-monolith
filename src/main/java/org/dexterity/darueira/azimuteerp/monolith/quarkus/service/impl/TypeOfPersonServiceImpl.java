package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfPerson;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.TypeOfPersonService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TypeOfPersonDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.TypeOfPersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class TypeOfPersonServiceImpl implements TypeOfPersonService {

    private final Logger log = LoggerFactory.getLogger(TypeOfPersonServiceImpl.class);

    @Inject
    TypeOfPersonMapper typeOfPersonMapper;

    @Override
    @Transactional
    public TypeOfPersonDTO persistOrUpdate(TypeOfPersonDTO typeOfPersonDTO) {
        log.debug("Request to save TypeOfPerson : {}", typeOfPersonDTO);
        var typeOfPerson = typeOfPersonMapper.toEntity(typeOfPersonDTO);
        typeOfPerson = TypeOfPerson.persistOrUpdate(typeOfPerson);
        return typeOfPersonMapper.toDto(typeOfPerson);
    }

    /**
     * Delete the TypeOfPerson by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete TypeOfPerson : {}", id);
        TypeOfPerson.findByIdOptional(id).ifPresent(typeOfPerson -> {
            typeOfPerson.delete();
        });
    }

    /**
     * Get one typeOfPerson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<TypeOfPersonDTO> findOne(Long id) {
        log.debug("Request to get TypeOfPerson : {}", id);
        return TypeOfPerson.findByIdOptional(id).map(typeOfPerson -> typeOfPersonMapper.toDto((TypeOfPerson) typeOfPerson));
    }

    /**
     * Get all the typeOfPeople.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<TypeOfPersonDTO> findAll(Page page) {
        log.debug("Request to get all TypeOfPeople");
        return new Paged<>(TypeOfPerson.findAll().page(page)).map(typeOfPerson -> typeOfPersonMapper.toDto((TypeOfPerson) typeOfPerson));
    }
}
