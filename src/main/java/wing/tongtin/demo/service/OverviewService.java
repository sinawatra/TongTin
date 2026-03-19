package wing.tongtin.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wing.tongtin.demo.enumeration.KycStatus;
import wing.tongtin.demo.repository.KycRepository;
import wing.tongtin.demo.repository.TontineGroupRepository;
import wing.tongtin.demo.repository.UserRepository;
import wing.tongtin.demo.response.OverviewResponse;

@Service
@RequiredArgsConstructor
public class OverviewService {

    private final UserRepository userRepository;
    private final TontineGroupRepository tontineGroupRepository;
    private final KycRepository kycRepository;

    public OverviewResponse getOverview() {
        long totalUsers = userRepository.count();
        long totalGroups = tontineGroupRepository.count();
        long totalMoney = userRepository.sumAllBalances().longValue();
        long totalPendingKyc = kycRepository.countByStatus(KycStatus.PENDING);

        return OverviewResponse.builder()
                .totalUser(totalUsers)
                .totalGroup(totalGroups)
                .totalMoney(totalMoney)
                .totalPendingKyc(totalPendingKyc)
                .build();
    }
}