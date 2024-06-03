package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

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
        article.customName = "customName1";
        article.assetsCollectionUUID = "assetsCollectionUUID1";
        return article;
    }

    public static Article getArticleSample2() {
        Article article = new Article();
        article.id = 2L;
        article.inventoryProductId = 2L;
        article.customName = "customName2";
        article.assetsCollectionUUID = "assetsCollectionUUID2";
        return article;
    }

    public static Article getArticleRandomSampleGenerator() {
        Article article = new Article();
        article.id = longCount.incrementAndGet();
        article.inventoryProductId = longCount.incrementAndGet();
        article.customName = UUID.randomUUID().toString();
        article.assetsCollectionUUID = UUID.randomUUID().toString();
        return article;
    }
}
