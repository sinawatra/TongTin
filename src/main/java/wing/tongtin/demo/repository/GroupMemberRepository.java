package wing.tongtin.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wing.tongtin.demo.entity.GroupMember;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

    List<GroupMember> findByGroupId(String groupId);
}