package za.co.patrick.finance.networth.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import za.co.patrick.finance.shared.domain.Money;

public record AssetRecord(
        UUID id,
        UUID profileId,
        String assetName,
        AssetType assetType,
        Money currentValue,
        Instant createdAt,
        Instant updatedAt
) {
    public AssetRecord {
        Objects.requireNonNull(id);
        Objects.requireNonNull(profileId);
        Objects.requireNonNull(assetName);
        Objects.requireNonNull(assetType);
        Objects.requireNonNull(currentValue);
        Objects.requireNonNull(createdAt);
        Objects.requireNonNull(updatedAt);
    }
}
