package org.dexterity.darueira.azimuteerp.monolith.quarkus.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "jhipster")
public interface JHipsterProperties {
    Info info();

    interface Info {
        Swagger swagger();

        interface Swagger {
            Boolean enable();
        }
    }
}
