package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Customer;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.CustomerService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CustomerDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.CustomerMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Inject
    CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerDTO persistOrUpdate(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        var customer = customerMapper.toEntity(customerDTO);
        customer = Customer.persistOrUpdate(customer);
        return customerMapper.toDto(customer);
    }

    /**
     * Delete the Customer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        Customer.findByIdOptional(id).ifPresent(customer -> {
            customer.delete();
        });
    }

    /**
     * Get one customer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CustomerDTO> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return Customer.findByIdOptional(id).map(customer -> customerMapper.toDto((Customer) customer));
    }

    /**
     * Get all the customers.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<CustomerDTO> findAll(Page page) {
        log.debug("Request to get all Customers");
        return new Paged<>(Customer.findAll().page(page)).map(customer -> customerMapper.toDto((Customer) customer));
    }
}
