package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Invoice getInvoiceSample1() {
        Invoice invoice = new Invoice();
        invoice.id = 1L;
        invoice.businessCode = "businessCode1";
        invoice.originalOrderId = 1L;
        invoice.description = "description1";
        invoice.numberOfInstallmentsOriginal = 1;
        invoice.numberOfInstallmentsPaid = 1;
        return invoice;
    }

    public static Invoice getInvoiceSample2() {
        Invoice invoice = new Invoice();
        invoice.id = 2L;
        invoice.businessCode = "businessCode2";
        invoice.originalOrderId = 2L;
        invoice.description = "description2";
        invoice.numberOfInstallmentsOriginal = 2;
        invoice.numberOfInstallmentsPaid = 2;
        return invoice;
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        Invoice invoice = new Invoice();
        invoice.id = longCount.incrementAndGet();
        invoice.businessCode = UUID.randomUUID().toString();
        invoice.originalOrderId = longCount.incrementAndGet();
        invoice.description = UUID.randomUUID().toString();
        invoice.numberOfInstallmentsOriginal = intCount.incrementAndGet();
        invoice.numberOfInstallmentsPaid = intCount.incrementAndGet();
        return invoice;
    }
}
