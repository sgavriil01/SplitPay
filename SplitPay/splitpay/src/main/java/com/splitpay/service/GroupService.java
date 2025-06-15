package com.splitpay.service;

import com.splitpay.dto.request.GroupRequest;
import com.splitpay.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {
    GroupResponse createGroup(GroupRequest request);
    List<GroupResponse> getAllGroups();
    GroupResponse getGroupById(Long id);
    GroupResponse updateGroup(Long id, GroupRequest request);
    void deleteGroup(Long id);
}
