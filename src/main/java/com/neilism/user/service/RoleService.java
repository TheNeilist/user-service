package com.neilism.user.service;

import com.neilism.user.model.Role;

import java.util.List;

public interface RoleService {

    public List<Role> findRolesByRoleIdList(List<Long> roleIds);

    public Role findByRole(String role);

}
