package za.co.patrick.finance.networth.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import za.co.patrick.finance.networth.domain.AssetType;

public record CreateAssetRecordRequest(
        @Schema(example = "Savings Account")
        @NotBlank(message = "assetName is required")
        String assetName,
        @Schema(example = "CASH")
        @NotNull(message = "assetType is required")
        AssetType assetType,
        @Schema(example = "5000.00")
        @NotNull(message = "currentValue is required")
        @DecimalMin(value = "0.00", message = "currentValue must be zero or greater")
        BigDecimal currentValue
) {
}
