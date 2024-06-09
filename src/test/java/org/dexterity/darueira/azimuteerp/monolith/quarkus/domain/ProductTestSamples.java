package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        Product product = new Product();
        product.id = 1L;
        product.productSKU = "productSKU1";
        product.productName = "productName1";
        product.description = "description1";
        product.reorderLevel = 1;
        product.targetLevel = 1;
        product.quantityPerUnit = "quantityPerUnit1";
        product.minimumReorderQuantity = 1;
        product.suggestedCategory = "suggestedCategory1";
        return product;
    }

    public static Product getProductSample2() {
        Product product = new Product();
        product.id = 2L;
        product.productSKU = "productSKU2";
        product.productName = "productName2";
        product.description = "description2";
        product.reorderLevel = 2;
        product.targetLevel = 2;
        product.quantityPerUnit = "quantityPerUnit2";
        product.minimumReorderQuantity = 2;
        product.suggestedCategory = "suggestedCategory2";
        return product;
    }

    public static Product getProductRandomSampleGenerator() {
        Product product = new Product();
        product.id = longCount.incrementAndGet();
        product.productSKU = UUID.randomUUID().toString();
        product.productName = UUID.randomUUID().toString();
        product.description = UUID.randomUUID().toString();
        product.reorderLevel = intCount.incrementAndGet();
        product.targetLevel = intCount.incrementAndGet();
        product.quantityPerUnit = UUID.randomUUID().toString();
        product.minimumReorderQuantity = intCount.incrementAndGet();
        product.suggestedCategory = UUID.randomUUID().toString();
        return product;
    }
}
