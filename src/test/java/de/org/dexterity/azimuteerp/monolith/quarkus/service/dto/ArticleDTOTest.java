package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleDTO.class);
        ArticleDTO articleDTO1 = new ArticleDTO();
        articleDTO1.id = 1L;
        ArticleDTO articleDTO2 = new ArticleDTO();
        assertThat(articleDTO1).isNotEqualTo(articleDTO2);
        articleDTO2.id = articleDTO1.id;
        assertThat(articleDTO1).isEqualTo(articleDTO2);
        articleDTO2.id = 2L;
        assertThat(articleDTO1).isNotEqualTo(articleDTO2);
        articleDTO1.id = null;
        assertThat(articleDTO1).isNotEqualTo(articleDTO2);
    }
}
