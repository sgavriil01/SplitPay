package com.splitpay.mapper;

import com.splitpay.dto.response.ExpenseResponse;
import com.splitpay.entity.Expense;
import java.util.stream.Collectors;

public class ExpenseMapper {
    public static ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            UserMapper.toResponse(expense.getPayer()),
            expense.getGroup().getId(),
            expense.getParticipants().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toSet()),
            expense.getCreatedAt()
        );
    }
}
