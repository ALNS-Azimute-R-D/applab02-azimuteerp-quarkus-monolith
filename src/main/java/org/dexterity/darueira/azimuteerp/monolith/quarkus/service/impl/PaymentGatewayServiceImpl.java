package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGateway;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.PaymentGatewayService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PaymentGatewayDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.PaymentGatewayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    private final Logger log = LoggerFactory.getLogger(PaymentGatewayServiceImpl.class);

    @Inject
    PaymentGatewayMapper paymentGatewayMapper;

    @Override
    @Transactional
    public PaymentGatewayDTO persistOrUpdate(PaymentGatewayDTO paymentGatewayDTO) {
        log.debug("Request to save PaymentGateway : {}", paymentGatewayDTO);
        var paymentGateway = paymentGatewayMapper.toEntity(paymentGatewayDTO);
        paymentGateway = PaymentGateway.persistOrUpdate(paymentGateway);
        return paymentGatewayMapper.toDto(paymentGateway);
    }

    /**
     * Delete the PaymentGateway by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete PaymentGateway : {}", id);
        PaymentGateway.findByIdOptional(id).ifPresent(paymentGateway -> {
            paymentGateway.delete();
        });
    }

    /**
     * Get one paymentGateway by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<PaymentGatewayDTO> findOne(Long id) {
        log.debug("Request to get PaymentGateway : {}", id);
        return PaymentGateway.findByIdOptional(id).map(paymentGateway -> paymentGatewayMapper.toDto((PaymentGateway) paymentGateway));
    }

    /**
     * Get all the paymentGateways.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<PaymentGatewayDTO> findAll(Page page) {
        log.debug("Request to get all PaymentGateways");
        return new Paged<>(PaymentGateway.findAll().page(page)).map(
            paymentGateway -> paymentGatewayMapper.toDto((PaymentGateway) paymentGateway)
        );
    }
}
