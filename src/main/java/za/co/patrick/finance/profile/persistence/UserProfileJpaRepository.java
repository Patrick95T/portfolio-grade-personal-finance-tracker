package za.co.patrick.finance.profile.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, UUID> {
}
