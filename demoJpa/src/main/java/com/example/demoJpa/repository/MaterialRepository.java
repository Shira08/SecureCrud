package com.example.demoJpa.repository;

import com.example.demoJpa.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material,Integer>  {
}
