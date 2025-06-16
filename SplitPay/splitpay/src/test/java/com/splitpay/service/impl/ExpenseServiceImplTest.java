package com.splitpay.service.impl;

import com.splitpay.dto.request.ExpenseRequest;
import com.splitpay.dto.response.ExpenseResponse;
import com.splitpay.entity.*;
import com.splitpay.exception.ResourceNotFoundException;
import com.splitpay.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExpense_shouldSaveAndReturnExpenseResponse() {
        // Setup
        Long payerId = 1L;
        Long groupId = 2L;
        Set<Long> participantIds = Set.of(3L, 4L);

        User payer = new User(1L, "Payer", "payer@example.com");
        Group group = Group.builder().id(groupId).name("Trip").build();
        Set<User> participants = Set.of(
                new User(3L, "U1", "u1@example.com"),
                new User(4L, "U2", "u2@example.com")
        );

        Expense expense = Expense.builder()
                .id(100L)
                .amount(BigDecimal.valueOf(100))
                .description("Lunch")
                .payer(payer)
                .group(group)
                .participants(participants)
                .createdAt(LocalDateTime.now())
                .build();

        // Mocks
        when(userRepository.findById(payerId)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findAllById(participantIds)).thenReturn(new ArrayList<>(participants));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        ExpenseRequest request = new ExpenseRequest(BigDecimal.valueOf(100), "Lunch", payerId, groupId, participantIds);
        ExpenseResponse response = expenseService.createExpense(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.amount()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(response.description()).isEqualTo("Lunch");
        assertThat(response.payer().id()).isEqualTo(1L);
        assertThat(response.groupId()).isEqualTo(groupId);
        assertThat(response.participants()).hasSize(2);
    }

    @Test
    void createExpense_missingPayer_shouldThrow() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ExpenseRequest request = new ExpenseRequest(BigDecimal.TEN, "x", 99L, 1L, Set.of(1L));
        assertThatThrownBy(() -> expenseService.createExpense(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Payer not found");
    }

    @Test
    void createExpense_missingGroup_shouldThrow() {
        User payer = new User(1L, "A", "a@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(42L)).thenReturn(Optional.empty());

        ExpenseRequest request = new ExpenseRequest(BigDecimal.TEN, "x", 1L, 42L, Set.of(1L));
        assertThatThrownBy(() -> expenseService.createExpense(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Group not found");
    }

    @Test
    void createExpense_missingParticipants_shouldThrow() {
        User payer = new User(1L, "A", "a@example.com");
        Group group = Group.builder().id(1L).name("Test").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findAllById(Set.of(2L))).thenReturn(Collections.emptyList());

        ExpenseRequest request = new ExpenseRequest(BigDecimal.TEN, "x", 1L, 1L, Set.of(2L));
        assertThatThrownBy(() -> expenseService.createExpense(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Some participants not found");
    }

    @Test
    void getAllExpenses_shouldReturnList() {
        Expense e1 = Expense.builder().id(1L).description("A").amount(BigDecimal.ONE).createdAt(LocalDateTime.now()).group(Group.builder().id(1L).name("G").build()).payer(new User(1L, "P", "p@example.com")).participants(Set.of()).build();
        Expense e2 = Expense.builder().id(2L).description("B").amount(BigDecimal.TEN).createdAt(LocalDateTime.now()).group(Group.builder().id(1L).name("G").build()).payer(new User(1L, "P", "p@example.com")).participants(Set.of()).build();

        when(expenseRepository.findAll()).thenReturn(List.of(e1, e2));

        var result = expenseService.getAllExpenses();
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteExpense_shouldCallRepo() {
        when(expenseRepository.existsById(99L)).thenReturn(true);
        expenseService.deleteExpense(99L);
        verify(expenseRepository).deleteById(99L);
    }

    @Test
    void deleteExpense_notFound_shouldThrow() {
        when(expenseRepository.existsById(42L)).thenReturn(false);
        assertThatThrownBy(() -> expenseService.deleteExpense(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Expense not found");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
