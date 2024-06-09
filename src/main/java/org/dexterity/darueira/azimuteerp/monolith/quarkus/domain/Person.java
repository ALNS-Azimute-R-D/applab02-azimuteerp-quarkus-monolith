package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.GenderEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "tb_person")
@Cacheable
@RegisterForReflection
public class Person extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "firstname", length = 50, nullable = false)
    public String firstname;

    @NotNull
    @Size(max = 50)
    @Column(name = "lastname", length = 50, nullable = false)
    public String lastname;

    @Size(max = 100)
    @Column(name = "fullname", length = 100)
    public String fullname;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    public LocalDate birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    public GenderEnum gender;

    @Size(max = 20)
    @Column(name = "code_bi", length = 20)
    public String codeBI;

    @Size(max = 20)
    @Column(name = "code_nif", length = 20)
    public String codeNIF;

    @NotNull
    @Size(max = 120)
    @Column(name = "street_address", length = 120, nullable = false)
    public String streetAddress;

    @Size(max = 20)
    @Column(name = "house_number", length = 20)
    public String houseNumber;

    @Size(max = 50)
    @Column(name = "location_name", length = 50)
    public String locationName;

    @NotNull
    @Size(max = 9)
    @Column(name = "postal_code", length = 9, nullable = false)
    public String postalCode;

    @NotNull
    @Size(max = 120)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "main_email", length = 120, nullable = false, unique = true)
    public String mainEmail;

    @Size(max = 15)
    @Column(name = "land_phone_number", length = 15)
    public String landPhoneNumber;

    @Size(max = 15)
    @Column(name = "mobile_phone_number", length = 15)
    public String mobilePhoneNumber;

    @Size(max = 40)
    @Column(name = "occupation", length = 40)
    public String occupation;

    @Size(max = 5)
    @Column(name = "preferred_language", length = 5)
    public String preferredLanguage;

    @Size(max = 40)
    @Column(name = "username_in_o_auth_2", length = 40)
    public String usernameInOAuth2;

    @Size(max = 80)
    @Column(name = "user_id_in_o_auth_2", length = 80)
    public String userIdInOAuth2;

    @Size(max = 2048)
    @Column(name = "custom_attributes_details_json", length = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @Lob
    @Column(name = "avatar_img")
    public byte[] avatarImg;

    @Column(name = "avatar_img_content_type")
    public String avatarImgContentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_of_person_id")
    @NotNull
    public TypeOfPerson typeOfPerson;

    @ManyToOne
    @JoinColumn(name = "district_id")
    public District district;

    @ManyToOne
    @JoinColumn(name = "manager_person_id")
    public Person managerPerson;

    @OneToMany(mappedBy = "managerPerson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Person> managedPersonsLists = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationMembership> organizationMembershipsLists = new HashSet<>();

    @OneToMany(mappedBy = "representativePerson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Supplier> suppliersLists = new HashSet<>();

    @OneToMany(mappedBy = "buyerPerson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Customer> customersLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Person{" +
            "id=" +
            id +
            ", firstname='" +
            firstname +
            "'" +
            ", lastname='" +
            lastname +
            "'" +
            ", fullname='" +
            fullname +
            "'" +
            ", birthDate='" +
            birthDate +
            "'" +
            ", gender='" +
            gender +
            "'" +
            ", codeBI='" +
            codeBI +
            "'" +
            ", codeNIF='" +
            codeNIF +
            "'" +
            ", streetAddress='" +
            streetAddress +
            "'" +
            ", houseNumber='" +
            houseNumber +
            "'" +
            ", locationName='" +
            locationName +
            "'" +
            ", postalCode='" +
            postalCode +
            "'" +
            ", mainEmail='" +
            mainEmail +
            "'" +
            ", landPhoneNumber='" +
            landPhoneNumber +
            "'" +
            ", mobilePhoneNumber='" +
            mobilePhoneNumber +
            "'" +
            ", occupation='" +
            occupation +
            "'" +
            ", preferredLanguage='" +
            preferredLanguage +
            "'" +
            ", usernameInOAuth2='" +
            usernameInOAuth2 +
            "'" +
            ", userIdInOAuth2='" +
            userIdInOAuth2 +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", avatarImg='" +
            avatarImg +
            "'" +
            ", avatarImgContentType='" +
            avatarImgContentType +
            "'" +
            "}"
        );
    }

    public Person update() {
        return update(this);
    }

    public Person persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Person update(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person can't be null");
        }
        var entity = Person.<Person>findById(person.id);
        if (entity != null) {
            entity.firstname = person.firstname;
            entity.lastname = person.lastname;
            entity.fullname = person.fullname;
            entity.birthDate = person.birthDate;
            entity.gender = person.gender;
            entity.codeBI = person.codeBI;
            entity.codeNIF = person.codeNIF;
            entity.streetAddress = person.streetAddress;
            entity.houseNumber = person.houseNumber;
            entity.locationName = person.locationName;
            entity.postalCode = person.postalCode;
            entity.mainEmail = person.mainEmail;
            entity.landPhoneNumber = person.landPhoneNumber;
            entity.mobilePhoneNumber = person.mobilePhoneNumber;
            entity.occupation = person.occupation;
            entity.preferredLanguage = person.preferredLanguage;
            entity.usernameInOAuth2 = person.usernameInOAuth2;
            entity.userIdInOAuth2 = person.userIdInOAuth2;
            entity.customAttributesDetailsJSON = person.customAttributesDetailsJSON;
            entity.activationStatus = person.activationStatus;
            entity.avatarImg = person.avatarImg;
            entity.typeOfPerson = person.typeOfPerson;
            entity.district = person.district;
            entity.managerPerson = person.managerPerson;
            entity.managedPersonsLists = person.managedPersonsLists;
            entity.organizationMembershipsLists = person.organizationMembershipsLists;
            entity.suppliersLists = person.suppliersLists;
            entity.customersLists = person.customersLists;
        }
        return entity;
    }

    public static Person persistOrUpdate(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person can't be null");
        }
        if (person.id == null) {
            persist(person);
            return person;
        } else {
            return update(person);
        }
    }
}
