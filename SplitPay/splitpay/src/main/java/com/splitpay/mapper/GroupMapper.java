package com.splitpay.mapper;

import com.splitpay.dto.request.GroupRequest;
import com.splitpay.dto.response.GroupResponse;
import com.splitpay.dto.response.UserResponse;
import com.splitpay.entity.Group;
import com.splitpay.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupMapper {

    public static Group toEntity(GroupRequest request, Set<User> users) {
        return Group.builder()
                .name(request.name())
                .users(users)
                .build();
    }

    public static GroupResponse toResponse(Group group) {
        Set<UserResponse> userResponses = group.getUsers()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toSet());

        return new GroupResponse(group.getId(), group.getName(), userResponses);
    }
}
