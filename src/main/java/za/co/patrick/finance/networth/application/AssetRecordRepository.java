package za.co.patrick.finance.networth.application;

import java.util.List;
import java.util.UUID;
import za.co.patrick.finance.networth.domain.AssetRecord;

public interface AssetRecordRepository {
    AssetRecord save(AssetRecord assetRecord);
    List<AssetRecord> findByProfileId(UUID profileId);
}
