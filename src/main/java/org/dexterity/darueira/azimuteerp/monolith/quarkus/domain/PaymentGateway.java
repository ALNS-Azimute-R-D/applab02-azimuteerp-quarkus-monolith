package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentGateway.
 */
@Entity
@Table(name = "tb_payment_gateway")
@Cacheable
@RegisterForReflection
public class PaymentGateway extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "alias_code", length = 20, nullable = false)
    public String aliasCode;

    @NotNull
    @Size(max = 120)
    @Column(name = "description", length = 120, nullable = false)
    public String description;

    @Size(max = 512)
    @Column(name = "business_handler_clazz", length = 512)
    public String businessHandlerClazz;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @OneToMany(mappedBy = "preferrablePaymentGateway")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Invoice> invoicesAsPreferrableLists = new HashSet<>();

    @OneToMany(mappedBy = "paymentGateway")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Payment> paymentsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentGateway)) {
            return false;
        }
        return id != null && id.equals(((PaymentGateway) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PaymentGateway{" +
            "id=" +
            id +
            ", aliasCode='" +
            aliasCode +
            "'" +
            ", description='" +
            description +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public PaymentGateway update() {
        return update(this);
    }

    public PaymentGateway persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static PaymentGateway update(PaymentGateway paymentGateway) {
        if (paymentGateway == null) {
            throw new IllegalArgumentException("paymentGateway can't be null");
        }
        var entity = PaymentGateway.<PaymentGateway>findById(paymentGateway.id);
        if (entity != null) {
            entity.aliasCode = paymentGateway.aliasCode;
            entity.description = paymentGateway.description;
            entity.businessHandlerClazz = paymentGateway.businessHandlerClazz;
            entity.activationStatus = paymentGateway.activationStatus;
            entity.invoicesAsPreferrableLists = paymentGateway.invoicesAsPreferrableLists;
            entity.paymentsLists = paymentGateway.paymentsLists;
        }
        return entity;
    }

    public static PaymentGateway persistOrUpdate(PaymentGateway paymentGateway) {
        if (paymentGateway == null) {
            throw new IllegalArgumentException("paymentGateway can't be null");
        }
        if (paymentGateway.id == null) {
            persist(paymentGateway);
            return paymentGateway;
        } else {
            return update(paymentGateway);
        }
    }
}
