package com.neilism.user.service;

import com.neilism.user.model.Role;
import com.neilism.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findRolesByRoleIdList(List<Long> roleIds) {
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> {
            final Role role = roleRepository.findRoleById(roleId);
            if (role != null) {
                roles.add(role);
            }
        });
        return roles;
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

}
