package com.example.atividade1.model;

import jakarta.persistence.*;

// Marca a classe como uma entidade JPA, que será mapeada para uma tabela no banco de dados
@Entity
@Table(name = "categories") // Define o nome da tabela no banco de dados
public class Category {

    // Define o campo 'id' como a chave primária da tabela
    @Id
    // Configura a geração automática do valor do 'id' pelo banco de dados
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Define o campo 'name' como uma coluna não nula e com valores únicos na tabela
    @Column(nullable = false, unique = true)
    private String name;

    // Define o campo 'rssUrl' com um comprimento máximo de 2048 caracteres
    @Column(length = 2048)
    private String rssUrl;

    // Construtor padrão necessário para JPA
    public Category() {}

    // Construtor para criar uma nova instância com nome e URL do feed RSS
    public Category(String name, String rssUrl) {
        this.name = name;
        this.rssUrl = rssUrl;
    }

    // Getters e Setters

    // Retorna o identificador da categoria
    public Long getId() {
        return id;
    }

    // Define o identificador da categoria
    public void setId(Long id) {
        this.id = id;
    }

    // Retorna a URL do feed RSS da categoria
    public String getRssUrl() {
        return rssUrl;
    }

    // Define a URL do feed RSS da categoria
    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    // Retorna o nome da categoria
    public String getName() {
        return name;
    }

    // Define o nome da categoria
    public void setName(String name) {
        this.name = name;
    }
}
