package wing.tongtin.demo.service;

import wing.tongtin.demo.entity.GroupMember;
import wing.tongtin.demo.entity.TontineGroupEntity;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.enumeration.GroupStatus;
import wing.tongtin.demo.repository.GroupMemberRepository;
import wing.tongtin.demo.repository.TontineGroupRepository;
import wing.tongtin.demo.repository.UserRepository;
import wing.tongtin.demo.request.CreateGroupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TontineService {

    private final TontineGroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public TontineGroupEntity createGroup(CreateGroupRequest request) {

        TontineGroupEntity group = TontineGroupEntity.builder()
                .groupName(request.getGroupName())
                .contributionAmount(request.getContributionAmount())
                .totalMembers(request.getTotalMembers())
                .status(GroupStatus.ACTIVE)
                .imageUrl(request.getImageUrl())
                .build();

        return groupRepository.save(group);
    }


    public List<TontineGroupEntity> getAllGroupListing() {
        return groupRepository.findAll();
    }

    public TontineGroupEntity getGroupInFoById(String id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
    }

    public GroupMember joinGroupById(String groupId) {
        // Get current authenticated user
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the group
        TontineGroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        // Check if user is already a member
        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new RuntimeException("User is already a member of this group");
        }

        // Check if group is full
        int currentMembers = groupMemberRepository.countByGroupId(groupId);
        if (currentMembers >= group.getTotalMembers()) {
            throw new RuntimeException("Group is full");
        }

        // Create new GroupMember with next payout order
        GroupMember member = GroupMember.builder()
                .user(user)
                .group(group)
                .payoutOrder(currentMembers + 1)
                .build();

        return groupMemberRepository.save(member);
    }

    public List<GroupMember> getGroupMembers(String groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }

}