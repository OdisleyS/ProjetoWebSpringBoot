package com.example.atividade1.repository;

import com.example.atividade1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interface que define um repositório para a entidade Category
// JpaRepository fornece operações básicas de CRUD para a entidade
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Método personalizado para encontrar uma categoria pelo nome
    // Retorna um Optional, que pode ou não conter uma Category
    Optional<Category> findByName(String name);
}
