package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Person;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.PersonService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PersonDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Inject
    PersonMapper personMapper;

    @Override
    @Transactional
    public PersonDTO persistOrUpdate(PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        var person = personMapper.toEntity(personDTO);
        person = Person.persistOrUpdate(person);
        return personMapper.toDto(person);
    }

    /**
     * Delete the Person by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        Person.findByIdOptional(id).ifPresent(person -> {
            person.delete();
        });
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<PersonDTO> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return Person.findByIdOptional(id).map(person -> personMapper.toDto((Person) person));
    }

    /**
     * Get all the people.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<PersonDTO> findAll(Page page) {
        log.debug("Request to get all People");
        return new Paged<>(Person.findAll().page(page)).map(person -> personMapper.toDto((Person) person));
    }
}
