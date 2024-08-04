package com.example.atividade1.model;

import jakarta.persistence.*;

// Define a entidade JPA para a tabela 'user_preferences'
@Entity
@Table(name = "user_preferences")
public class UserPreference {

    // Define o campo 'id' como chave primária e gera seu valor automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Define um relacionamento muitos-para-um com a entidade 'User'
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Define um relacionamento muitos-para-um com a entidade 'Category'
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    // Métodos getters e setters para acessar e modificar os campos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
