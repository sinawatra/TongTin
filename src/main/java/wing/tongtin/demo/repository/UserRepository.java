package wing.tongtin.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wing.tongtin.demo.entity.UserEntity;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findById(String id);

    @Query("SELECT COALESCE(SUM(u.balance), 0) FROM UserEntity u")
    BigDecimal sumAllBalances();
}