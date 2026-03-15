package wing.tongtin.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wing.tongtin.demo.entity.ContributionEntity;

import java.util.List;
import java.util.Optional;

public interface ContributionRepository extends JpaRepository<ContributionEntity, String> {

    List<ContributionEntity> findByGroupId(String groupId);

    List<ContributionEntity> findByGroupIdAndUserId(String groupId, String userId);

    List<ContributionEntity> findByGroupIdAndCycleNumber(String groupId, Integer cycleNumber);

    Optional<ContributionEntity> findByGroupIdAndUserIdAndCycleNumber(String groupId, String userId, Integer cycleNumber);

    int countByGroupIdAndCycleNumber(String groupId, Integer cycleNumber);
}