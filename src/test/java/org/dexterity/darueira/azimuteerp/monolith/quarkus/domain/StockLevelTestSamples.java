package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockLevelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StockLevel getStockLevelSample1() {
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = 1L;
        stockLevel.remainingQuantity = 1;
        stockLevel.commonAttributesDetailsJSON = "commonAttributesDetailsJSON1";
        return stockLevel;
    }

    public static StockLevel getStockLevelSample2() {
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = 2L;
        stockLevel.remainingQuantity = 2;
        stockLevel.commonAttributesDetailsJSON = "commonAttributesDetailsJSON2";
        return stockLevel;
    }

    public static StockLevel getStockLevelRandomSampleGenerator() {
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = longCount.incrementAndGet();
        stockLevel.remainingQuantity = intCount.incrementAndGet();
        stockLevel.commonAttributesDetailsJSON = UUID.randomUUID().toString();
        return stockLevel;
    }
}
