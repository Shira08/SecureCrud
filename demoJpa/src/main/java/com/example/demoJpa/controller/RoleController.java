package com.example.demoJpa.controller;

import com.example.demoJpa.entity.Role;
import com.example.demoJpa.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestBody Role role) {

        if (role.getName() == null || role.getName().isEmpty() ||
                role.getDescription() == null || role.getDescription().isEmpty()) {
            return new ResponseEntity<>("Name and description must not be empty", HttpStatus.BAD_REQUEST);
        }
        Role savedRole = roleService.saveRole(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable Integer id) {
        try {
            Role role = roleService.getRole(id);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Role with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            return new ResponseEntity<>("Deleted role id - " + id, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Role with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}

