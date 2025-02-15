package com.codesoft.edu.service.impl;

import com.codesoft.edu.service.RoleService;
import com.codesoft.edu.model.Role;
import com.codesoft.edu.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
            return roleRepository.save(role);
    }

    @Override
    public Role readById(long id) {
        Optional<Role> optional = roleRepository.findById(id);
            return optional.get();
    }

    @Override
    public Role update(Role role) {
            Role oldRole = readById(role.getId());
                return roleRepository.save(role);
    }

    @Override
    public void delete(long id) {
        Role role = readById(id);
            roleRepository.delete(role);
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }
}
