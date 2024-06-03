package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Tenant;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.TenantService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TenantDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.TenantMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    @Inject
    TenantMapper tenantMapper;

    @Override
    @Transactional
    public TenantDTO persistOrUpdate(TenantDTO tenantDTO) {
        log.debug("Request to save Tenant : {}", tenantDTO);
        var tenant = tenantMapper.toEntity(tenantDTO);
        tenant = Tenant.persistOrUpdate(tenant);
        return tenantMapper.toDto(tenant);
    }

    /**
     * Delete the Tenant by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Tenant : {}", id);
        Tenant.findByIdOptional(id).ifPresent(tenant -> {
            tenant.delete();
        });
    }

    /**
     * Get one tenant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<TenantDTO> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return Tenant.findByIdOptional(id).map(tenant -> tenantMapper.toDto((Tenant) tenant));
    }

    /**
     * Get all the tenants.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<TenantDTO> findAll(Page page) {
        log.debug("Request to get all Tenants");
        return new Paged<>(Tenant.findAll().page(page)).map(tenant -> tenantMapper.toDto((Tenant) tenant));
    }
}
