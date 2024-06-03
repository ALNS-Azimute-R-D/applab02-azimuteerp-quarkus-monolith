package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Person getPersonSample1() {
        Person person = new Person();
        person.id = 1L;
        person.firstName = "firstName1";
        person.lastName = "lastName1";
        person.codeBI = "codeBI1";
        person.codeNIF = "codeNIF1";
        person.streetAddress = "streetAddress1";
        person.houseNumber = "houseNumber1";
        person.locationName = "locationName1";
        person.postalCode = "postalCode1";
        person.mainEmail = "mainEmail1";
        person.landPhoneNumber = "landPhoneNumber1";
        person.mobilePhoneNumber = "mobilePhoneNumber1";
        person.occupation = "occupation1";
        person.preferredLanguage = "preferredLanguage1";
        person.usernameInOAuth2 = "usernameInOAuth21";
        person.userIdInOAuth2 = "userIdInOAuth21";
        return person;
    }

    public static Person getPersonSample2() {
        Person person = new Person();
        person.id = 2L;
        person.firstName = "firstName2";
        person.lastName = "lastName2";
        person.codeBI = "codeBI2";
        person.codeNIF = "codeNIF2";
        person.streetAddress = "streetAddress2";
        person.houseNumber = "houseNumber2";
        person.locationName = "locationName2";
        person.postalCode = "postalCode2";
        person.mainEmail = "mainEmail2";
        person.landPhoneNumber = "landPhoneNumber2";
        person.mobilePhoneNumber = "mobilePhoneNumber2";
        person.occupation = "occupation2";
        person.preferredLanguage = "preferredLanguage2";
        person.usernameInOAuth2 = "usernameInOAuth22";
        person.userIdInOAuth2 = "userIdInOAuth22";
        return person;
    }

    public static Person getPersonRandomSampleGenerator() {
        Person person = new Person();
        person.id = longCount.incrementAndGet();
        person.firstName = UUID.randomUUID().toString();
        person.lastName = UUID.randomUUID().toString();
        person.codeBI = UUID.randomUUID().toString();
        person.codeNIF = UUID.randomUUID().toString();
        person.streetAddress = UUID.randomUUID().toString();
        person.houseNumber = UUID.randomUUID().toString();
        person.locationName = UUID.randomUUID().toString();
        person.postalCode = UUID.randomUUID().toString();
        person.mainEmail = UUID.randomUUID().toString();
        person.landPhoneNumber = UUID.randomUUID().toString();
        person.mobilePhoneNumber = UUID.randomUUID().toString();
        person.occupation = UUID.randomUUID().toString();
        person.preferredLanguage = UUID.randomUUID().toString();
        person.usernameInOAuth2 = UUID.randomUUID().toString();
        person.userIdInOAuth2 = UUID.randomUUID().toString();
        return person;
    }
}
