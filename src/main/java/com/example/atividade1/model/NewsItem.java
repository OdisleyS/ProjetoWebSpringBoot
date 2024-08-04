package com.example.atividade1.model;

import jakarta.persistence.*;
import java.util.Date;

// Marca a classe como uma entidade JPA, que será mapeada para uma tabela no banco de dados
@Entity
@Table(name = "news_items") // Define o nome da tabela no banco de dados
public class NewsItem {

    // Define o campo 'id' como a chave primária da tabela
    @Id
    // Configura a geração automática do valor do 'id' pelo banco de dados
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Define o campo 'title' como uma coluna obrigatória e que pode armazenar texto longo
    @Column(nullable = false, columnDefinition="TEXT")
    private String title;

    // Define o campo 'imageUrl' como uma coluna que pode armazenar texto longo
    @Column(columnDefinition="TEXT")
    private String imageUrl;

    // Define o campo 'link' como uma coluna obrigatória e que pode armazenar texto longo
    @Column(nullable = false, columnDefinition="TEXT")
    private String link;

    // Define o campo 'publicationDate' como uma coluna obrigatória e que armazena data e hora
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // Garante que a data e hora sejam armazenadas corretamente
    private Date publicationDate;

    // Define o relacionamento muitos-para-um com a entidade Category
    @ManyToOne(fetch = FetchType.EAGER) // Define o carregamento da categoria como imediato (EAGER)
    @JoinColumn(name = "category_id") // Define a coluna que armazena a referência à categoria
    private Category category;

    // Getters e Setters

    // Retorna o identificador da notícia
    public Long getId() {
        return id;
    }

    // Define o identificador da notícia
    public void setId(Long id) {
        this.id = id;
    }

    // Retorna o título da notícia
    public String getTitle() {
        return title;
    }

    // Define o título da notícia
    public void setTitle(String title) {
        this.title = title;
    }

    // Retorna a URL da imagem da notícia
    public String getImageUrl() {
        return imageUrl;
    }

    // Define a URL da imagem da notícia
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Retorna o link da notícia
    public String getLink() {
        return link;
    }

    // Define o link da notícia
    public void setLink(String link) {
        this.link = link;
    }

    // Retorna a data de publicação da notícia
    public Date getPublicationDate() {
        return publicationDate;
    }

    // Define a data de publicação da notícia
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    // Retorna a categoria da notícia
    public Category getCategory() {
        return category;
    }

    // Define a categoria da notícia
    public void setCategory(Category category) {
        this.category = category;
    }
}
