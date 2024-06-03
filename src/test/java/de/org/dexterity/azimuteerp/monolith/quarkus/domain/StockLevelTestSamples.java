package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockLevelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StockLevel getStockLevelSample1() {
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = 1L;
        stockLevel.ramainingQuantity = 1;
        return stockLevel;
    }

    public static StockLevel getStockLevelSample2() {
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = 2L;
        stockLevel.ramainingQuantity = 2;
        return stockLevel;
    }

    public static StockLevel getStockLevelRandomSampleGenerator() {
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = longCount.incrementAndGet();
        stockLevel.ramainingQuantity = intCount.incrementAndGet();
        return stockLevel;
    }
}
