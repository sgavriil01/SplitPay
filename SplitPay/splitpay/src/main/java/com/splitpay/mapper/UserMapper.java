package com.splitpay.mapper;

import com.splitpay.dto.request.UserRequest;
import com.splitpay.dto.response.UserResponse;
import com.splitpay.entity.User;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
