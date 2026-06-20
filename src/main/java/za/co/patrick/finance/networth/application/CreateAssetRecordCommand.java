package za.co.patrick.finance.networth.application;

import java.math.BigDecimal;
import java.util.UUID;
import za.co.patrick.finance.networth.domain.AssetType;

public record CreateAssetRecordCommand(
        UUID profileId,
        String assetName,
        AssetType assetType,
        BigDecimal currentValue
) {
}
