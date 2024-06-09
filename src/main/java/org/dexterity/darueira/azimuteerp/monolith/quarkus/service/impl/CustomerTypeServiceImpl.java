package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerType;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.CustomerTypeService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CustomerTypeDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.CustomerTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeServiceImpl.class);

    @Inject
    CustomerTypeMapper customerTypeMapper;

    @Override
    @Transactional
    public CustomerTypeDTO persistOrUpdate(CustomerTypeDTO customerTypeDTO) {
        log.debug("Request to save CustomerType : {}", customerTypeDTO);
        var customerType = customerTypeMapper.toEntity(customerTypeDTO);
        customerType = CustomerType.persistOrUpdate(customerType);
        return customerTypeMapper.toDto(customerType);
    }

    /**
     * Delete the CustomerType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CustomerType : {}", id);
        CustomerType.findByIdOptional(id).ifPresent(customerType -> {
            customerType.delete();
        });
    }

    /**
     * Get one customerType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CustomerTypeDTO> findOne(Long id) {
        log.debug("Request to get CustomerType : {}", id);
        return CustomerType.findByIdOptional(id).map(customerType -> customerTypeMapper.toDto((CustomerType) customerType));
    }

    /**
     * Get all the customerTypes.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<CustomerTypeDTO> findAll(Page page) {
        log.debug("Request to get all CustomerTypes");
        return new Paged<>(CustomerType.findAll().page(page)).map(customerType -> customerTypeMapper.toDto((CustomerType) customerType));
    }
}
