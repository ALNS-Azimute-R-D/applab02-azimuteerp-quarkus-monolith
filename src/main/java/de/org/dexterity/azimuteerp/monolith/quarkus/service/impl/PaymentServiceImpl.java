package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Payment;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.PaymentService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PaymentDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.PaymentMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Inject
    PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentDTO persistOrUpdate(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        var payment = paymentMapper.toEntity(paymentDTO);
        payment = Payment.persistOrUpdate(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Delete the Payment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        Payment.findByIdOptional(id).ifPresent(payment -> {
            payment.delete();
        });
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return Payment.findByIdOptional(id).map(payment -> paymentMapper.toDto((Payment) payment));
    }

    /**
     * Get all the payments.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<PaymentDTO> findAll(Page page) {
        log.debug("Request to get all Payments");
        return new Paged<>(Payment.findAll().page(page)).map(payment -> paymentMapper.toDto((Payment) payment));
    }
}
