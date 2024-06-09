package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { PaymentGatewayMapper.class })
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(source = "paymentGateway.id", target = "paymentGatewayId")
    @Mapping(source = "paymentGateway.aliasCode", target = "paymentGateway")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "paymentGatewayId", target = "paymentGateway")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.id = id;
        return payment;
    }
}
