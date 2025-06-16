package com.splitpay.service;

import com.splitpay.dto.request.ExpenseRequest;
import com.splitpay.dto.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request);
    List<ExpenseResponse> getAllExpenses();
    List<ExpenseResponse> getExpensesByGroup(Long groupId);
    void deleteExpense(Long id);
}
