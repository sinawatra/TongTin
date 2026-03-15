package wing.tongtin.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wing.tongtin.demo.entity.PayoutEntity;

import java.util.List;
import java.util.Optional;

public interface PayoutRepository extends JpaRepository<PayoutEntity, String> {

    List<PayoutEntity> findByGroupId(String groupId);

    List<PayoutEntity> findByRecipientId(String recipientId);

    Optional<PayoutEntity> findByGroupIdAndCycleNumber(String groupId, Integer cycleNumber);

    int countByGroupId(String groupId);
}