package com.splitpay.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

public record ExpenseRequest(
    @NotNull BigDecimal amount,
    @NotBlank String description,
    @NotNull Long payerId,
    @NotNull Long groupId,
    @NotEmpty Set<Long> participantIds
) {}
