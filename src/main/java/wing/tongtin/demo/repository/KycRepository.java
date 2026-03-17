package wing.tongtin.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wing.tongtin.demo.entity.KycEntity;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.enumeration.KycDocumentType;
import wing.tongtin.demo.enumeration.KycStatus;

import java.util.List;
import java.util.Optional;

public interface KycRepository extends JpaRepository<KycEntity, String> {

    List<KycEntity> findByUser(UserEntity user);

    List<KycEntity> findByUserId(String userId);

    List<KycEntity> findByStatus(KycStatus status);

    Optional<KycEntity> findByUserAndType(UserEntity user, KycDocumentType type);

    boolean existsByUserAndType(UserEntity user, KycDocumentType type);
}