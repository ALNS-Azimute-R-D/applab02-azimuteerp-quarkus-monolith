package de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In Native / Integration tests, injects Keycloak server connection details.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectKeycloakServer {
}
