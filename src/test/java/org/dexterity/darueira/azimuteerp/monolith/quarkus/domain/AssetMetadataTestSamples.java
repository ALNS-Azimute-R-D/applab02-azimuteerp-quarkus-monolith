package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetMetadataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetMetadata getAssetMetadataSample1() {
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = 1L;
        assetMetadata.metadataDetailsJSON = "metadataDetailsJSON1";
        return assetMetadata;
    }

    public static AssetMetadata getAssetMetadataSample2() {
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = 2L;
        assetMetadata.metadataDetailsJSON = "metadataDetailsJSON2";
        return assetMetadata;
    }

    public static AssetMetadata getAssetMetadataRandomSampleGenerator() {
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = longCount.incrementAndGet();
        assetMetadata.metadataDetailsJSON = UUID.randomUUID().toString();
        return assetMetadata;
    }
}
