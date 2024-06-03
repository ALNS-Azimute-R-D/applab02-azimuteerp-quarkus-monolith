package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDTO.class);
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.id = 1L;
        OrderDTO orderDTO2 = new OrderDTO();
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
        orderDTO2.id = orderDTO1.id;
        assertThat(orderDTO1).isEqualTo(orderDTO2);
        orderDTO2.id = 2L;
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
        orderDTO1.id = null;
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
    }
}
