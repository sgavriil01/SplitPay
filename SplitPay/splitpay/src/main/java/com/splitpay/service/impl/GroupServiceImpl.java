package com.splitpay.service.impl;

import com.splitpay.dto.request.GroupRequest;
import com.splitpay.dto.response.GroupResponse;
import com.splitpay.entity.Group;
import com.splitpay.entity.User;
import com.splitpay.exception.ResourceNotFoundException;
import com.splitpay.mapper.GroupMapper;
import com.splitpay.repository.GroupRepository;
import com.splitpay.repository.UserRepository;
import com.splitpay.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public GroupResponse createGroup(GroupRequest request) {
        Set<User> users = userRepository.findAllById(request.userIds()).stream().collect(Collectors.toSet());

        if (users.size() != request.userIds().size()) {
            throw new ResourceNotFoundException("One or more users not found");
        }

        Group group = GroupMapper.toEntity(request, users);
        return GroupMapper.toResponse(groupRepository.save(group));
    }

    @Override
    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupMapper::toResponse)
                .toList();
    }

    @Override
    public GroupResponse getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        return GroupMapper.toResponse(group);
    }

    @Override
    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Set<User> users = userRepository.findAllById(request.userIds()).stream().collect(Collectors.toSet());
        group.setName(request.name());
        group.setUsers(users);

        return GroupMapper.toResponse(groupRepository.save(group));
    }

    @Override
    public void deleteGroup(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Group not found");
        }
        groupRepository.deleteById(id);
    }
}
