package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class StockLevelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockLevelDTO.class);
        StockLevelDTO stockLevelDTO1 = new StockLevelDTO();
        stockLevelDTO1.id = 1L;
        StockLevelDTO stockLevelDTO2 = new StockLevelDTO();
        assertThat(stockLevelDTO1).isNotEqualTo(stockLevelDTO2);
        stockLevelDTO2.id = stockLevelDTO1.id;
        assertThat(stockLevelDTO1).isEqualTo(stockLevelDTO2);
        stockLevelDTO2.id = 2L;
        assertThat(stockLevelDTO1).isNotEqualTo(stockLevelDTO2);
        stockLevelDTO1.id = null;
        assertThat(stockLevelDTO1).isNotEqualTo(stockLevelDTO2);
    }
}
