package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetType getAssetTypeSample1() {
        AssetType assetType = new AssetType();
        assetType.id = 1L;
        assetType.acronym = "acronym1";
        assetType.name = "name1";
        assetType.description = "description1";
        assetType.handlerClazzName = "handlerClazzName1";
        return assetType;
    }

    public static AssetType getAssetTypeSample2() {
        AssetType assetType = new AssetType();
        assetType.id = 2L;
        assetType.acronym = "acronym2";
        assetType.name = "name2";
        assetType.description = "description2";
        assetType.handlerClazzName = "handlerClazzName2";
        return assetType;
    }

    public static AssetType getAssetTypeRandomSampleGenerator() {
        AssetType assetType = new AssetType();
        assetType.id = longCount.incrementAndGet();
        assetType.acronym = UUID.randomUUID().toString();
        assetType.name = UUID.randomUUID().toString();
        assetType.description = UUID.randomUUID().toString();
        assetType.handlerClazzName = UUID.randomUUID().toString();
        return assetType;
    }
}
