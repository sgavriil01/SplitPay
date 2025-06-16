package com.splitpay.service.impl;

import com.splitpay.dto.request.ExpenseRequest;
import com.splitpay.dto.response.ExpenseResponse;
import com.splitpay.entity.*;
import com.splitpay.exception.ResourceNotFoundException;
import com.splitpay.mapper.ExpenseMapper;
import com.splitpay.repository.*;
import com.splitpay.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        User payer = userRepository.findById(request.payerId())
                .orElseThrow(() -> new ResourceNotFoundException("Payer not found"));

        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Set<User> participants = userRepository.findAllById(request.participantIds())
                .stream().collect(Collectors.toSet());

        if (participants.size() != request.participantIds().size()) {
            throw new ResourceNotFoundException("Some participants not found");
        }

        Expense expense = Expense.builder()
                .amount(request.amount())
                .description(request.description())
                .payer(payer)
                .group(group)
                .participants(participants)
                .createdAt(LocalDateTime.now())
                .build();

        return ExpenseMapper.toResponse(expenseRepository.save(expense));
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByGroup(Long groupId) {
        return expenseRepository.findByGroupId(groupId).stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found");
        }
        expenseRepository.deleteById(id);
    }
}
