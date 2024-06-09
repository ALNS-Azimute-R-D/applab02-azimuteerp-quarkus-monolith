package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PaymentGatewayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentGateway} and its DTO {@link PaymentGatewayDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface PaymentGatewayMapper extends EntityMapper<PaymentGatewayDTO, PaymentGateway> {
    @Mapping(target = "invoicesAsPreferrableLists", ignore = true)
    @Mapping(target = "paymentsLists", ignore = true)
    PaymentGateway toEntity(PaymentGatewayDTO paymentGatewayDTO);

    default PaymentGateway fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentGateway paymentGateway = new PaymentGateway();
        paymentGateway.id = id;
        return paymentGateway;
    }
}
