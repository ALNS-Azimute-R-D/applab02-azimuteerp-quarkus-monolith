package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.PaymentStatusEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.PaymentTypeEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

/**
 * A Payment.
 */
@Entity
@Table(name = "tb_payment")
@Cacheable
@RegisterForReflection
public class Payment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "installment_number", nullable = false)
    public Integer installmentNumber;

    @NotNull
    @Column(name = "payment_due_date", nullable = false)
    public Instant paymentDueDate;

    @NotNull
    @Column(name = "payment_paid_date", nullable = false)
    public Instant paymentPaidDate;

    @NotNull
    @Column(name = "payment_amount", precision = 21, scale = 2, nullable = false)
    public BigDecimal paymentAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_payment", nullable = false)
    public PaymentTypeEnum typeOfPayment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public PaymentStatusEnum status;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "extra_details", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String extraDetails;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_method_id")
    @NotNull
    public PaymentMethod paymentMethod;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Payment{" +
            "id=" +
            id +
            ", installmentNumber=" +
            installmentNumber +
            ", paymentDueDate='" +
            paymentDueDate +
            "'" +
            ", paymentPaidDate='" +
            paymentPaidDate +
            "'" +
            ", paymentAmount=" +
            paymentAmount +
            ", typeOfPayment='" +
            typeOfPayment +
            "'" +
            ", status='" +
            status +
            "'" +
            ", extraDetails='" +
            extraDetails +
            "'" +
            "}"
        );
    }

    public Payment update() {
        return update(this);
    }

    public Payment persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Payment update(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("payment can't be null");
        }
        var entity = Payment.<Payment>findById(payment.id);
        if (entity != null) {
            entity.installmentNumber = payment.installmentNumber;
            entity.paymentDueDate = payment.paymentDueDate;
            entity.paymentPaidDate = payment.paymentPaidDate;
            entity.paymentAmount = payment.paymentAmount;
            entity.typeOfPayment = payment.typeOfPayment;
            entity.status = payment.status;
            entity.extraDetails = payment.extraDetails;
            entity.paymentMethod = payment.paymentMethod;
        }
        return entity;
    }

    public static Payment persistOrUpdate(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("payment can't be null");
        }
        if (payment.id == null) {
            persist(payment);
            return payment;
        } else {
            return update(payment);
        }
    }
}
