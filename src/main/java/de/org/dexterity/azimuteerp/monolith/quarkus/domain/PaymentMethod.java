package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentMethod.
 */
@Entity
@Table(name = "tb_payment_method")
@Cacheable
@RegisterForReflection
public class PaymentMethod extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "code", length = 10, nullable = false)
    public String code;

    @NotNull
    @Size(max = 40)
    @Column(name = "description", length = 40, nullable = false)
    public String description;

    @Size(max = 512)
    @Column(name = "business_handler_clazz", length = 512)
    public String businessHandlerClazz;

    @OneToMany(mappedBy = "preferrablePaymentMethod")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Invoice> invoicesAsPreferrableLists = new HashSet<>();

    @OneToMany(mappedBy = "paymentMethod")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Payment> paymentsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethod)) {
            return false;
        }
        return id != null && id.equals(((PaymentMethod) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PaymentMethod{" +
            "id=" +
            id +
            ", code='" +
            code +
            "'" +
            ", description='" +
            description +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            "}"
        );
    }

    public PaymentMethod update() {
        return update(this);
    }

    public PaymentMethod persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static PaymentMethod update(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("paymentMethod can't be null");
        }
        var entity = PaymentMethod.<PaymentMethod>findById(paymentMethod.id);
        if (entity != null) {
            entity.code = paymentMethod.code;
            entity.description = paymentMethod.description;
            entity.businessHandlerClazz = paymentMethod.businessHandlerClazz;
            entity.invoicesAsPreferrableLists = paymentMethod.invoicesAsPreferrableLists;
            entity.paymentsLists = paymentMethod.paymentsLists;
        }
        return entity;
    }

    public static PaymentMethod persistOrUpdate(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("paymentMethod can't be null");
        }
        if (paymentMethod.id == null) {
            persist(paymentMethod);
            return paymentMethod;
        } else {
            return update(paymentMethod);
        }
    }
}
