package com.example.demoJpa.service;

import com.example.demoJpa.entity.Role;
import com.example.demoJpa.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRole(Integer id) {
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
    }

    public void deleteRole(Integer id){
        roleRepository.deleteById(id);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }
}
