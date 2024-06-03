package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TenantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantDTO.class);
        TenantDTO tenantDTO1 = new TenantDTO();
        tenantDTO1.id = 1L;
        TenantDTO tenantDTO2 = new TenantDTO();
        assertThat(tenantDTO1).isNotEqualTo(tenantDTO2);
        tenantDTO2.id = tenantDTO1.id;
        assertThat(tenantDTO1).isEqualTo(tenantDTO2);
        tenantDTO2.id = 2L;
        assertThat(tenantDTO1).isNotEqualTo(tenantDTO2);
        tenantDTO1.id = null;
        assertThat(tenantDTO1).isNotEqualTo(tenantDTO2);
    }
}
