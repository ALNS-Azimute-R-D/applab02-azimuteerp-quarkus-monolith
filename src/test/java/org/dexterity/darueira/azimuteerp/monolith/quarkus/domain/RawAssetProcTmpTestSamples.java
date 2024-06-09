package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RawAssetProcTmpTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RawAssetProcTmp getRawAssetProcTmpSample1() {
        RawAssetProcTmp rawAssetProcTmp = new RawAssetProcTmp();
        rawAssetProcTmp.id = 1L;
        rawAssetProcTmp.name = "name1";
        rawAssetProcTmp.fullFilenamePath = "fullFilenamePath1";
        rawAssetProcTmp.customAttributesDetailsJSON = "customAttributesDetailsJSON1";
        return rawAssetProcTmp;
    }

    public static RawAssetProcTmp getRawAssetProcTmpSample2() {
        RawAssetProcTmp rawAssetProcTmp = new RawAssetProcTmp();
        rawAssetProcTmp.id = 2L;
        rawAssetProcTmp.name = "name2";
        rawAssetProcTmp.fullFilenamePath = "fullFilenamePath2";
        rawAssetProcTmp.customAttributesDetailsJSON = "customAttributesDetailsJSON2";
        return rawAssetProcTmp;
    }

    public static RawAssetProcTmp getRawAssetProcTmpRandomSampleGenerator() {
        RawAssetProcTmp rawAssetProcTmp = new RawAssetProcTmp();
        rawAssetProcTmp.id = longCount.incrementAndGet();
        rawAssetProcTmp.name = UUID.randomUUID().toString();
        rawAssetProcTmp.fullFilenamePath = UUID.randomUUID().toString();
        rawAssetProcTmp.customAttributesDetailsJSON = UUID.randomUUID().toString();
        return rawAssetProcTmp;
    }
}
