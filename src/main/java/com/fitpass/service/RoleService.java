package com.fitpass.service;

import com.fitpass.entity.Role;
import com.fitpass.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + id));
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role update(Long id, Role role) {
        Role existingRole = findById(id);

        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());

        return roleRepository.save(existingRole);
    }

    public void delete(Long id) {
        Role role = findById(id);
        roleRepository.delete(role);
    }
}
