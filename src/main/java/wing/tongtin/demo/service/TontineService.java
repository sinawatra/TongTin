package wing.tongtin.demo.service;

import wing.tongtin.demo.entity.TontineGroup;
import wing.tongtin.demo.enumeration.GroupStatus;
import wing.tongtin.demo.repository.TontineGroupRepository;
import wing.tongtin.demo.request.CreateGroupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TontineService {

    private final TontineGroupRepository groupRepository;

    public TontineGroup createGroup(CreateGroupRequest request) {

        TontineGroup group = TontineGroup.builder()
                .groupName(request.getGroupName())
                .contributionAmount(request.getContributionAmount())
                .totalMembers(request.getTotalMembers())
                .status(GroupStatus.PENDING)
                .build();

        return groupRepository.save(group);
    }


    public List<TontineGroup> getAllGroupListing() {
        return groupRepository.findAll();
    }

}