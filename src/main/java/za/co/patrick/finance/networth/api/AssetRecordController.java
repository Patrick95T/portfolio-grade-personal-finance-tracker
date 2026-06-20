package za.co.patrick.finance.networth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.patrick.finance.networth.application.AssetRecordService;
import za.co.patrick.finance.networth.application.CreateAssetRecordCommand;
import za.co.patrick.finance.networth.domain.AssetRecord;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/assets")
@Tag(name = "Assets", description = "Track assets used in net worth and planning")
public class AssetRecordController {
    private final AssetRecordService assetRecordService;

    public AssetRecordController(AssetRecordService assetRecordService) {
        this.assetRecordService = assetRecordService;
    }

    @PostMapping
    @Operation(summary = "Create an asset record")
    public ResponseEntity<AssetRecordResponse> createAssetRecord(@PathVariable UUID profileId, @Valid @RequestBody CreateAssetRecordRequest request) {
        var asset = assetRecordService.createAssetRecord(new CreateAssetRecordCommand(
                profileId, request.assetName(), request.assetType(), request.currentValue()
        ));
        return ResponseEntity.created(URI.create("/api/v1/profiles/" + profileId + "/assets/" + asset.id())).body(toResponse(asset));
    }

    @GetMapping
    @Operation(summary = "List asset records for a profile")
    public List<AssetRecordResponse> listAssetRecords(@PathVariable UUID profileId) {
        return assetRecordService.listAssetRecords(profileId).stream().map(AssetRecordController::toResponse).toList();
    }

    private static AssetRecordResponse toResponse(AssetRecord asset) {
        return new AssetRecordResponse(asset.id(), asset.profileId(), asset.assetName(), asset.assetType(),
                asset.currentValue().amount(), asset.createdAt(), asset.updatedAt());
    }
}
