package de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration;

/**
 * The StorageTypeEnum enumeration.
 */
public enum StorageTypeEnum {
    BLOB_IN_DB,
    LOCAL_FILESYSTEM,
    LOCAL_MINIO,
    REMOTE_FILESYSTEM,
    REMOTE_MINIO,
    AWS_S3,
    OTHER_CLOUD_STORAGE,
}
