package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.InvoiceStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Type;

/**
 * - Invoice
 * - PaymentMethod
 * - Payment
 */
@Entity
@Table(name = "tb_invoice")
@Cacheable
@RegisterForReflection
public class Invoice extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "business_code", length = 15, nullable = false)
    public String businessCode;

    @Column(name = "original_order_id")
    public Long originalOrderId;

    @Column(name = "invoice_date")
    public Instant invoiceDate;

    @Column(name = "due_date")
    public Instant dueDate;

    @NotNull
    @Size(max = 80)
    @Column(name = "description", length = 80, nullable = false)
    public String description;

    @Column(name = "tax_value", precision = 21, scale = 2)
    public BigDecimal taxValue;

    @Column(name = "shipping_value", precision = 21, scale = 2)
    public BigDecimal shippingValue;

    @Column(name = "amount_due_value", precision = 21, scale = 2)
    public BigDecimal amountDueValue;

    @NotNull
    @Column(name = "number_of_installments_original", nullable = false)
    public Integer numberOfInstallmentsOriginal;

    @Column(name = "number_of_installments_paid")
    public Integer numberOfInstallmentsPaid;

    @Column(name = "amount_paid_value", precision = 21, scale = 2)
    public BigDecimal amountPaidValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public InvoiceStatusEnum status;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "extra_details")
    public String extraDetails;

    @ManyToOne
    @JoinColumn(name = "preferrable_payment_method_id")
    public PaymentMethod preferrablePaymentMethod;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Invoice{" +
            "id=" +
            id +
            ", businessCode='" +
            businessCode +
            "'" +
            ", originalOrderId=" +
            originalOrderId +
            ", invoiceDate='" +
            invoiceDate +
            "'" +
            ", dueDate='" +
            dueDate +
            "'" +
            ", description='" +
            description +
            "'" +
            ", taxValue=" +
            taxValue +
            ", shippingValue=" +
            shippingValue +
            ", amountDueValue=" +
            amountDueValue +
            ", numberOfInstallmentsOriginal=" +
            numberOfInstallmentsOriginal +
            ", numberOfInstallmentsPaid=" +
            numberOfInstallmentsPaid +
            ", amountPaidValue=" +
            amountPaidValue +
            ", status='" +
            status +
            "'" +
            ", extraDetails='" +
            extraDetails +
            "'" +
            "}"
        );
    }

    public Invoice update() {
        return update(this);
    }

    public Invoice persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Invoice update(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("invoice can't be null");
        }
        var entity = Invoice.<Invoice>findById(invoice.id);
        if (entity != null) {
            entity.businessCode = invoice.businessCode;
            entity.originalOrderId = invoice.originalOrderId;
            entity.invoiceDate = invoice.invoiceDate;
            entity.dueDate = invoice.dueDate;
            entity.description = invoice.description;
            entity.taxValue = invoice.taxValue;
            entity.shippingValue = invoice.shippingValue;
            entity.amountDueValue = invoice.amountDueValue;
            entity.numberOfInstallmentsOriginal = invoice.numberOfInstallmentsOriginal;
            entity.numberOfInstallmentsPaid = invoice.numberOfInstallmentsPaid;
            entity.amountPaidValue = invoice.amountPaidValue;
            entity.status = invoice.status;
            entity.extraDetails = invoice.extraDetails;
            entity.preferrablePaymentMethod = invoice.preferrablePaymentMethod;
        }
        return entity;
    }

    public static Invoice persistOrUpdate(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("invoice can't be null");
        }
        if (invoice.id == null) {
            persist(invoice);
            return invoice;
        } else {
            return update(invoice);
        }
    }
}
