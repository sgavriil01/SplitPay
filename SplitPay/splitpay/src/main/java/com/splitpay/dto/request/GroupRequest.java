package com.splitpay.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record GroupRequest(
    @NotBlank String name,
    @NotEmpty Set<Long> userIds
) {}
