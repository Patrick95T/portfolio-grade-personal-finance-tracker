package za.co.patrick.finance.networth.application;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.networth.domain.AssetRecord;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class AssetRecordService {
    private final AssetRecordRepository assetRecordRepository;
    private final UserProfileService userProfileService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public AssetRecordService(
            AssetRecordRepository assetRecordRepository,
            UserProfileService userProfileService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.assetRecordRepository = assetRecordRepository;
        this.userProfileService = userProfileService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public AssetRecord createAssetRecord(CreateAssetRecordCommand command) {
        var profile = userProfileService.getProfile(command.profileId());
        Instant now = Instant.now(clock);
        var asset = new AssetRecord(
                UUID.randomUUID(),
                command.profileId(),
                command.assetName().trim(),
                command.assetType(),
                new Money(command.currentValue(), profile.preferredCurrency()),
                now,
                now
        );
        var savedAsset = assetRecordRepository.save(asset);
        auditEventService.record(
                savedAsset.profileId(),
                AuditEventType.ASSET_CREATED,
                "AssetRecord",
                savedAsset.id(),
                "Created asset record " + savedAsset.assetName()
        );
        return savedAsset;
    }

    @Transactional(readOnly = true)
    public List<AssetRecord> listAssetRecords(UUID profileId) {
        userProfileService.getProfile(profileId);
        return assetRecordRepository.findByProfileId(profileId);
    }
}
