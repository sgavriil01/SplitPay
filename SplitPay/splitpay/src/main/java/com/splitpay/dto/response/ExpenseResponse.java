package com.splitpay.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record ExpenseResponse(
    Long id,
    BigDecimal amount,
    String description,
    UserResponse payer,
    Long groupId,
    Set<UserResponse> participants,
    LocalDateTime createdAt
) {}
