package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.ArticleTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollectionTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CategoryTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrderItemTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = getArticleSample1();
        Article article2 = new Article();
        assertThat(article1).isNotEqualTo(article2);

        article2.id = article1.id;
        assertThat(article1).isEqualTo(article2);

        article2 = getArticleSample2();
        assertThat(article1).isNotEqualTo(article2);
    }
}
