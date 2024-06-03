package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.PaymentMethod;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.PaymentMethodService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PaymentMethodDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.PaymentMethodMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    @Inject
    PaymentMethodMapper paymentMethodMapper;

    @Override
    @Transactional
    public PaymentMethodDTO persistOrUpdate(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to save PaymentMethod : {}", paymentMethodDTO);
        var paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = PaymentMethod.persistOrUpdate(paymentMethod);
        return paymentMethodMapper.toDto(paymentMethod);
    }

    /**
     * Delete the PaymentMethod by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethod : {}", id);
        PaymentMethod.findByIdOptional(id).ifPresent(paymentMethod -> {
            paymentMethod.delete();
        });
    }

    /**
     * Get one paymentMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<PaymentMethodDTO> findOne(Long id) {
        log.debug("Request to get PaymentMethod : {}", id);
        return PaymentMethod.findByIdOptional(id).map(paymentMethod -> paymentMethodMapper.toDto((PaymentMethod) paymentMethod));
    }

    /**
     * Get all the paymentMethods.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<PaymentMethodDTO> findAll(Page page) {
        log.debug("Request to get all PaymentMethods");
        return new Paged<>(PaymentMethod.findAll().page(page)).map(
            paymentMethod -> paymentMethodMapper.toDto((PaymentMethod) paymentMethod)
        );
    }
}
