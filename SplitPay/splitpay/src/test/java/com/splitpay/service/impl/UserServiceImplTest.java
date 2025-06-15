package com.splitpay.service.impl;

import com.splitpay.dto.request.UserRequest;
import com.splitpay.dto.response.UserResponse;
import com.splitpay.entity.User;
import com.splitpay.exception.ResourceNotFoundException;
import com.splitpay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.*;
import java.util.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldSaveAndReturnUserResponse() {
        // given
        UserRequest request = new UserRequest("Alice", "alice@example.com");
        User user = User.builder().id(1L).name("Alice").email("alice@example.com").build();

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponse response = userService.createUser(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Alice");
        assertThat(response.email()).isEqualTo("alice@example.com");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnListOfUserResponses() {
        // given
        List<User> users = List.of(
                new User(1L, "A", "a@example.com"),
                new User(2L, "B", "b@example.com"));
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<UserResponse> result = userService.getAllUsers();

        // then
        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUser() {
        // given
        User user = new User(1L, "Test", "test@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        UserResponse response = userService.getUserById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_whenUserNotFound_shouldThrow() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateUser_shouldUpdateAndReturnUserResponse() {
        // given
        User existing = new User(1L, "Old", "old@example.com");
        UserRequest updated = new UserRequest("New", "new@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // when
        UserResponse result = userService.updateUser(1L, updated);

        // then
        assertThat(result.name()).isEqualTo("New");
        assertThat(result.email()).isEqualTo("new@example.com");
    }

    @Test
    void deleteUser_shouldDeleteIfExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_whenNotFound_shouldThrow() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
