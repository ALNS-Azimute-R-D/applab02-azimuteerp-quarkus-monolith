package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { PaymentGatewayMapper.class })
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(source = "preferrablePaymentGateway.id", target = "preferrablePaymentGatewayId")
    @Mapping(source = "preferrablePaymentGateway.aliasCode", target = "preferrablePaymentGateway")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "preferrablePaymentGatewayId", target = "preferrablePaymentGateway")
    @Mapping(target = "ordersLists", ignore = true)
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.id = id;
        return invoice;
    }
}
