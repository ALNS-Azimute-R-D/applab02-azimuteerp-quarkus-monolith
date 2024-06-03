package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { PaymentMethodMapper.class })
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(source = "preferrablePaymentMethod.id", target = "preferrablePaymentMethodId")
    @Mapping(source = "preferrablePaymentMethod.code", target = "preferrablePaymentMethod")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "preferrablePaymentMethodId", target = "preferrablePaymentMethod")
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
