package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Article getArticleSample1() {
        Article article = new Article();
        article.id = 1L;
        article.inventoryProductId = 1L;
        article.skuCode = "skuCode1";
        article.customName = "customName1";
        article.customDescription = "customDescription1";
        return article;
    }

    public static Article getArticleSample2() {
        Article article = new Article();
        article.id = 2L;
        article.inventoryProductId = 2L;
        article.skuCode = "skuCode2";
        article.customName = "customName2";
        article.customDescription = "customDescription2";
        return article;
    }

    public static Article getArticleRandomSampleGenerator() {
        Article article = new Article();
        article.id = longCount.incrementAndGet();
        article.inventoryProductId = longCount.incrementAndGet();
        article.skuCode = UUID.randomUUID().toString();
        article.customName = UUID.randomUUID().toString();
        article.customDescription = UUID.randomUUID().toString();
        return article;
    }
}
