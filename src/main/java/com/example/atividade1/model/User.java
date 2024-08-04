package com.example.atividade1.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Define a classe como uma entidade JPA, mapeada para a tabela 'users' no banco de dados
@Entity
@Table(name = "users") // Nome da tabela no banco de dados
public class User {

    // Define o campo 'id' como a chave primária da tabela
    @Id
    // Configura a geração automática do valor do 'id' pelo banco de dados
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Define o campo 'name' como uma coluna obrigatória (não pode ser nula)
    @Column(nullable = false)
    private String name;

    // Define o campo 'email' como uma coluna obrigatória e única (não pode ser nula e deve ser único)
    @Column(nullable = false, unique = true)
    private String email;

    // Define o campo 'login' como uma coluna obrigatória e única (não pode ser nula e deve ser único)
    @Column(nullable = false, unique = true)
    private String login;

    // Define o campo 'password' como uma coluna obrigatória (não pode ser nula)
    @Column(nullable = false)
    private String password;

    // Define o campo 'phone' como uma coluna obrigatória (não pode ser nula)
    @Column(nullable = false)
    private String phone;

    // Define o campo 'birthDate' como uma coluna obrigatória (não pode ser nula) e armazena a data
    @Column(nullable = false)
    @Temporal(TemporalType.DATE) // Garante que apenas a data (sem hora) seja armazenada
    private Date birthDate;

    // Define o campo 'status' como uma coluna obrigatória e usa enumeração para representar os estados
    @Enumerated(EnumType.STRING) // Armazena o valor do enum como uma string
    @Column(nullable = false)
    private Status status;

    // Define o relacionamento muitos-para-muitos com a entidade 'Category'
    @ManyToMany(fetch = FetchType.LAZY) // Carregamento preguiçoso das categorias favoritas
    @JoinTable(
            name = "user_favorite_categories", // Nome da tabela intermediária
            joinColumns = @JoinColumn(name = "user_id"), // Coluna de referência para o usuário
            inverseJoinColumns = @JoinColumn(name = "category_id") // Coluna de referência para a categoria
    )
    private Set<Category> favoriteCategories = new HashSet<>(); // Inicializa o conjunto de categorias favoritas

    // Enumeração para representar os possíveis estados do usuário
    public enum Status {
        ACTIVE, INACTIVE
    }

    // Getters e Setters

    // Retorna o identificador do usuário
    public Long getId() {
        return id;
    }

    // Define o identificador do usuário
    public void setId(Long id) {
        this.id = id;
    }

    // Retorna o nome do usuário
    public String getName() {
        return name;
    }

    // Define o nome do usuário
    public void setName(String name) {
        this.name = name;
    }

    // Retorna o e-mail do usuário
    public String getEmail() {
        return email;
    }

    // Define o e-mail do usuário
    public void setEmail(String email) {
        this.email = email;
    }

    // Retorna o login do usuário
    public String getLogin() {
        return login;
    }

    // Define o login do usuário
    public void setLogin(String login) {
        this.login = login;
    }

    // Retorna a senha do usuário
    public String getPassword() {
        return password;
    }

    // Define a senha do usuário
    public void setPassword(String password) {
        this.password = password;
    }

    // Retorna o telefone do usuário
    public String getPhone() {
        return phone;
    }

    // Define o telefone do usuário
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Retorna a data de nascimento do usuário
    public Date getBirthDate() {
        return birthDate;
    }

    // Define a data de nascimento do usuário
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    // Retorna o status do usuário
    public Status getStatus() {
        return status;
    }

    // Define o status do usuário
    public void setStatus(Status status) {
        this.status = status;
    }

    // Retorna o conjunto de categorias favoritas do usuário
    public Set<Category> getFavoriteCategories() {
        return favoriteCategories;
    }

    // Define o conjunto de categorias favoritas do usuário
    public void setFavoriteCategories(Set<Category> favoriteCategories) {
        this.favoriteCategories = favoriteCategories;
    }
}
