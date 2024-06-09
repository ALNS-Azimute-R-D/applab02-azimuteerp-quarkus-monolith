package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeOfPersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeOfPerson getTypeOfPersonSample1() {
        TypeOfPerson typeOfPerson = new TypeOfPerson();
        typeOfPerson.id = 1L;
        typeOfPerson.code = "code1";
        typeOfPerson.description = "description1";
        return typeOfPerson;
    }

    public static TypeOfPerson getTypeOfPersonSample2() {
        TypeOfPerson typeOfPerson = new TypeOfPerson();
        typeOfPerson.id = 2L;
        typeOfPerson.code = "code2";
        typeOfPerson.description = "description2";
        return typeOfPerson;
    }

    public static TypeOfPerson getTypeOfPersonRandomSampleGenerator() {
        TypeOfPerson typeOfPerson = new TypeOfPerson();
        typeOfPerson.id = longCount.incrementAndGet();
        typeOfPerson.code = UUID.randomUUID().toString();
        typeOfPerson.description = UUID.randomUUID().toString();
        return typeOfPerson;
    }
}
