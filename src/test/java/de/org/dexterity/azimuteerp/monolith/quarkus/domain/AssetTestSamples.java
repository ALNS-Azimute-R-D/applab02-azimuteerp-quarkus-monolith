package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Asset getAssetSample1() {
        Asset asset = new Asset();
        asset.id = 1L;
        asset.uid = UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa");
        asset.name = "name1";
        asset.fullFilenamePath = "fullFilenamePath1";
        return asset;
    }

    public static Asset getAssetSample2() {
        Asset asset = new Asset();
        asset.id = 2L;
        asset.uid = UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367");
        asset.name = "name2";
        asset.fullFilenamePath = "fullFilenamePath2";
        return asset;
    }

    public static Asset getAssetRandomSampleGenerator() {
        Asset asset = new Asset();
        asset.id = longCount.incrementAndGet();
        asset.uid = UUID.randomUUID();
        asset.name = UUID.randomUUID().toString();
        asset.fullFilenamePath = UUID.randomUUID().toString();
        return asset;
    }
}
