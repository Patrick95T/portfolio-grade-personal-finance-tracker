package za.co.patrick.finance.networth.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.networth.application.AssetRecordRepository;
import za.co.patrick.finance.networth.domain.AssetRecord;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class AssetRecordPersistenceAdapter implements AssetRecordRepository {
    private final AssetRecordJpaRepository assetRecordJpaRepository;

    public AssetRecordPersistenceAdapter(AssetRecordJpaRepository assetRecordJpaRepository) {
        this.assetRecordJpaRepository = assetRecordJpaRepository;
    }

    @Override
    public AssetRecord save(AssetRecord assetRecord) {
        return toDomain(assetRecordJpaRepository.save(toEntity(assetRecord)));
    }

    @Override
    public List<AssetRecord> findByProfileId(UUID profileId) {
        return assetRecordJpaRepository.findByProfileIdOrderByCreatedAtAsc(profileId).stream().map(AssetRecordPersistenceAdapter::toDomain).toList();
    }

    private static AssetRecordEntity toEntity(AssetRecord assetRecord) {
        var entity = new AssetRecordEntity();
        entity.setId(assetRecord.id());
        entity.setProfileId(assetRecord.profileId());
        entity.setAssetName(assetRecord.assetName());
        entity.setAssetType(assetRecord.assetType());
        entity.setCurrentValue(assetRecord.currentValue().amount());
        entity.setCurrency(assetRecord.currentValue().currency());
        entity.setCreatedAt(assetRecord.createdAt());
        entity.setUpdatedAt(assetRecord.updatedAt());
        return entity;
    }

    private static AssetRecord toDomain(AssetRecordEntity entity) {
        return new AssetRecord(entity.getId(), entity.getProfileId(), entity.getAssetName(), entity.getAssetType(),
                new Money(entity.getCurrentValue(), entity.getCurrency()), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
