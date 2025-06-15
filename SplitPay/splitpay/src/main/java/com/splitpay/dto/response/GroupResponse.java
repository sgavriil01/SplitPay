package com.splitpay.dto.response;

import java.util.Set;

public record GroupResponse(
    Long id,
    String name,
    Set<UserResponse> users
) {}
