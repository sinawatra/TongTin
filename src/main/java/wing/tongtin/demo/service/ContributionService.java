package wing.tongtin.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wing.tongtin.demo.entity.*;
import wing.tongtin.demo.enumeration.GroupStatus;
import wing.tongtin.demo.enumeration.PaymentStatus;
import wing.tongtin.demo.repository.*;
import wing.tongtin.demo.request.ContributeRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;
    private final PayoutRepository payoutRepository;
    private final TontineGroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public ContributionEntity contribute(String groupId, ContributeRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TontineGroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        // Check if user is a member of the group
        if (!groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new RuntimeException("User is not a member of this group");
        }

        // Check if group is active
        if (group.getStatus() != GroupStatus.ACTIVE) {
            throw new RuntimeException("Group is not active. Current status: " + group.getStatus());
        }

        // Validate contribution amount matches group's required amount
        if (request.getAmount().compareTo(group.getContributionAmount()) != 0) {
            throw new RuntimeException("Contribution amount must be exactly " + group.getContributionAmount());
        }

        // Determine current cycle (based on completed payouts + 1)
        int currentCycle = payoutRepository.countByGroupId(groupId) + 1;

        // Check if user already contributed for this cycle
        if (contributionRepository.findByGroupIdAndUserIdAndCycleNumber(groupId, userId, currentCycle).isPresent()) {
            throw new RuntimeException("User has already contributed for cycle " + currentCycle);
        }

        ContributionEntity contribution = ContributionEntity.builder()
                .user(user)
                .group(group)
                .amount(request.getAmount())
                .cycleNumber(currentCycle)
                .paymentStatus(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now())
                .build();

        return contributionRepository.save(contribution);
    }

    public List<ContributionEntity> getGroupContributions(String groupId) {
        // Verify group exists
        groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        return contributionRepository.findByGroupId(groupId);
    }

    public List<ContributionEntity> getUserContributions(String groupId, String userId) {
        // Verify group exists
        groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return contributionRepository.findByGroupIdAndUserId(groupId, userId);
    }

    @Transactional
    public PayoutEntity triggerPayout(String groupId) {
        TontineGroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        // Check if group is active
        if (group.getStatus() != GroupStatus.ACTIVE) {
            throw new RuntimeException("Group is not active. Current status: " + group.getStatus());
        }

        // Determine current cycle
        int completedPayouts = payoutRepository.countByGroupId(groupId);
        int currentCycle = completedPayouts + 1;

        // Check if payout already exists for this cycle
        if (payoutRepository.findByGroupIdAndCycleNumber(groupId, currentCycle).isPresent()) {
            throw new RuntimeException("Payout already processed for cycle " + currentCycle);
        }

        // Get all members
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        int totalMembers = members.size();

        if (totalMembers == 0) {
            throw new RuntimeException("No members in the group");
        }

        // Check if all members have contributed for this cycle
        int contributionsForCycle = contributionRepository.countByGroupIdAndCycleNumber(groupId, currentCycle);
        if (contributionsForCycle < totalMembers) {
            throw new RuntimeException("Not all members have contributed for cycle " + currentCycle +
                    ". Contributions: " + contributionsForCycle + "/" + totalMembers);
        }

        // Find the member whose turn it is to receive payout (based on payoutOrder)
        GroupMember recipient = members.stream()
                .filter(m -> m.getPayoutOrder() == currentCycle)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No member found for payout order " + currentCycle));

        // Calculate total payout amount
        BigDecimal payoutAmount = group.getContributionAmount().multiply(BigDecimal.valueOf(totalMembers));

        PayoutEntity payout = PayoutEntity.builder()
                .recipient(recipient.getUser())
                .group(group)
                .cycleNumber(currentCycle)
                .amount(payoutAmount)
                .status(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now())
                .build();

        PayoutEntity savedPayout = payoutRepository.save(payout);

        // Check if all cycles are complete
        if (currentCycle >= totalMembers) {
            group.setStatus(GroupStatus.COMPLETED);
            groupRepository.save(group);
        }

        return savedPayout;
    }
}