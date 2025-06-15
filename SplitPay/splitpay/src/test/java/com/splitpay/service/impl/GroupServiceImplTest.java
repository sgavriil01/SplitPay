package com.splitpay.service.impl;

import com.splitpay.dto.request.GroupRequest;
import com.splitpay.dto.response.GroupResponse;
import com.splitpay.entity.Group;
import com.splitpay.entity.User;
import com.splitpay.exception.ResourceNotFoundException;
import com.splitpay.repository.GroupRepository;
import com.splitpay.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGroup_shouldSaveAndReturnGroupResponse() {
        // given
        Set<Long> userIds = Set.of(1L, 2L);
        Set<User> users = Set.of(
            new User(1L, "User1", "u1@example.com"),
            new User(2L, "User2", "u2@example.com")
        );
        GroupRequest request = new GroupRequest("Trip", userIds);
        Group group = Group.builder().id(1L).name("Trip").users(users).build();

        when(userRepository.findAllById(userIds)).thenReturn(new ArrayList<>(users));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        // when
        GroupResponse response = groupService.createGroup(request);

        // then
        assertThat(response.name()).isEqualTo("Trip");
        assertThat(response.users()).hasSize(2);
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void getGroupById_shouldReturnGroup() {
        Group group = Group.builder()
            .id(1L)
            .name("Test Group")
            .users(Set.of())
            .build();

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        GroupResponse response = groupService.getGroupById(1L);

        assertThat(response.name()).isEqualTo("Test Group");
    }

    @Test
    void getGroupById_notFound_shouldThrow() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.getGroupById(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Group not found");
    }

    @Test
    void updateGroup_shouldUpdateNameAndUsers() {
        Group existing = Group.builder().id(1L).name("Old").users(Set.of()).build();
        Set<User> newUsers = Set.of(new User(3L, "User3", "u3@example.com"));
        GroupRequest request = new GroupRequest("New", Set.of(3L));

        when(groupRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findAllById(Set.of(3L))).thenReturn(new ArrayList<>(newUsers));
        when(groupRepository.save(any(Group.class))).thenAnswer(inv -> inv.getArgument(0));

        GroupResponse result = groupService.updateGroup(1L, request);

        assertThat(result.name()).isEqualTo("New");
        assertThat(result.users()).hasSize(1);
    }

    @Test
    void deleteGroup_shouldDeleteIfExists() {
        when(groupRepository.existsById(1L)).thenReturn(true);

        groupService.deleteGroup(1L);

        verify(groupRepository).deleteById(1L);
    }

    @Test
    void deleteGroup_whenNotExists_shouldThrow() {
        when(groupRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> groupService.deleteGroup(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Group not found");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
