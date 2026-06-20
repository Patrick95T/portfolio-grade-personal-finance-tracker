package za.co.patrick.finance.networth.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AssetRecordJpaRepository extends JpaRepository<AssetRecordEntity, UUID> {
    List<AssetRecordEntity> findByProfileIdOrderByCreatedAtAsc(UUID profileId);
}
