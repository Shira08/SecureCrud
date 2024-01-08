package com.example.demoJpa.controller;

import com.example.demoJpa.entity.Material;
import com.example.demoJpa.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/materials")
public class MaterialController {
    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('material:read')")
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.findAll();
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('material:read')")
    public ResponseEntity<Material> getMaterialById(@PathVariable Integer id) {
        Optional<Material> material = materialService.findById(id);
        if (material.isPresent()) {
            return new ResponseEntity<>(material.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('material:create')")
    public ResponseEntity<Material> addMaterial(@RequestBody Material material) {
        Material savedMaterial = materialService.save(material);
        return new ResponseEntity<>(savedMaterial, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('material:update')")
    public ResponseEntity<Material> updateMaterial(@PathVariable Integer id, @RequestBody Material updatedMaterial) {
        Optional<Material> material = materialService.findById(id);
        if (material.isPresent()) {
            Material savedMaterial = materialService.save(updatedMaterial);
            return new ResponseEntity<>(savedMaterial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('material:delete')")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Integer id) {
        materialService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
