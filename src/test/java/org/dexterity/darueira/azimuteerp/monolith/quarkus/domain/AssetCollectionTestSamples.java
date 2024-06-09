package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetCollectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetCollection getAssetCollectionSample1() {
        AssetCollection assetCollection = new AssetCollection();
        assetCollection.id = 1L;
        assetCollection.name = "name1";
        assetCollection.fullFilenamePath = "fullFilenamePath1";
        return assetCollection;
    }

    public static AssetCollection getAssetCollectionSample2() {
        AssetCollection assetCollection = new AssetCollection();
        assetCollection.id = 2L;
        assetCollection.name = "name2";
        assetCollection.fullFilenamePath = "fullFilenamePath2";
        return assetCollection;
    }

    public static AssetCollection getAssetCollectionRandomSampleGenerator() {
        AssetCollection assetCollection = new AssetCollection();
        assetCollection.id = longCount.incrementAndGet();
        assetCollection.name = UUID.randomUUID().toString();
        assetCollection.fullFilenamePath = UUID.randomUUID().toString();
        return assetCollection;
    }
}
