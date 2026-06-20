package za.co.patrick.finance.networth.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import za.co.patrick.finance.networth.domain.AssetType;

public record AssetRecordResponse(
        UUID id,
        UUID profileId,
        String assetName,
        AssetType assetType,
        BigDecimal currentValue,
        Instant createdAt,
        Instant updatedAt
) {
}
