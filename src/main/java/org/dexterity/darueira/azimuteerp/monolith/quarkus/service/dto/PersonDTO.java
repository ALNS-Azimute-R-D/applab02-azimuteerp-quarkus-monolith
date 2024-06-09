package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.config.Constants;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.GenderEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Person} entity.
 */
@RegisterForReflection
public class PersonDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 50)
    public String firstname;

    @NotNull
    @Size(max = 50)
    public String lastname;

    @Size(max = 100)
    public String fullname;

    @NotNull
    @JsonbDateFormat(value = Constants.LOCAL_DATE_FORMAT)
    public LocalDate birthDate;

    @NotNull
    public GenderEnum gender;

    @Size(max = 20)
    public String codeBI;

    @Size(max = 20)
    public String codeNIF;

    @NotNull
    @Size(max = 120)
    public String streetAddress;

    @Size(max = 20)
    public String houseNumber;

    @Size(max = 50)
    public String locationName;

    @NotNull
    @Size(max = 9)
    public String postalCode;

    @NotNull
    @Size(max = 120)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    public String mainEmail;

    @Size(max = 15)
    public String landPhoneNumber;

    @Size(max = 15)
    public String mobilePhoneNumber;

    @Size(max = 40)
    public String occupation;

    @Size(max = 5)
    public String preferredLanguage;

    @Size(max = 40)
    public String usernameInOAuth2;

    @Size(max = 80)
    public String userIdInOAuth2;

    @Size(max = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    public ActivationStatusEnum activationStatus;

    @Lob
    public byte[] avatarImg;

    public String avatarImgContentType;

    public Long typeOfPersonId;
    public String typeOfPerson;
    public Long districtId;
    public String district;
    public Long managerPersonId;
    public String managerPerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDTO)) {
            return false;
        }

        return id != null && id.equals(((PersonDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PersonDTO{" +
            ", id=" +
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
            ", typeOfPersonId=" +
            typeOfPersonId +
            ", typeOfPerson='" +
            typeOfPerson +
            "'" +
            ", districtId=" +
            districtId +
            ", district='" +
            district +
            "'" +
            ", managerPersonId=" +
            managerPersonId +
            ", managerPerson='" +
            managerPerson +
            "'" +
            "}"
        );
    }
}
