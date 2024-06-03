package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AssetMetadataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetMetadata getAssetMetadataSample1() {
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = 1L;
        return assetMetadata;
    }

    public static AssetMetadata getAssetMetadataSample2() {
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = 2L;
        return assetMetadata;
    }

    public static AssetMetadata getAssetMetadataRandomSampleGenerator() {
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = longCount.incrementAndGet();
        return assetMetadata;
    }
}
