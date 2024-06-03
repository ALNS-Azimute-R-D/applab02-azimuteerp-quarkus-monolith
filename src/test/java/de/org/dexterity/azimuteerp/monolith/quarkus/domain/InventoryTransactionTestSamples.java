package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InventoryTransaction getInventoryTransactionSample1() {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.id = 1L;
        inventoryTransaction.invoiceId = 1L;
        inventoryTransaction.quantity = 1;
        inventoryTransaction.comments = "comments1";
        return inventoryTransaction;
    }

    public static InventoryTransaction getInventoryTransactionSample2() {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.id = 2L;
        inventoryTransaction.invoiceId = 2L;
        inventoryTransaction.quantity = 2;
        inventoryTransaction.comments = "comments2";
        return inventoryTransaction;
    }

    public static InventoryTransaction getInventoryTransactionRandomSampleGenerator() {
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.id = longCount.incrementAndGet();
        inventoryTransaction.invoiceId = longCount.incrementAndGet();
        inventoryTransaction.quantity = intCount.incrementAndGet();
        inventoryTransaction.comments = UUID.randomUUID().toString();
        return inventoryTransaction;
    }
}
