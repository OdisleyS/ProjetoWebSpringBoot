package com.example.atividade1.repository;

import com.example.atividade1.model.Category;
import com.example.atividade1.model.NewsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {

    // Método para buscar todas as notícias de uma categoria específica
    List<NewsItem> findAllByCategory(Category category);

    // Método para buscar todas as notícias pertencentes a várias categorias
    List<NewsItem> findAllByCategoryIn(Set<Category> categories);

    // Método que usa uma consulta personalizada para ordenar notícias por data de publicação (mais recentes primeiro)
    @Query("SELECT n FROM NewsItem n ORDER BY n.publicationDate DESC")
    List<NewsItem> findAllOrderByPublicationDateDesc();

    // Método para encontrar uma notícia específica baseada no título e no link
    Optional<NewsItem> findByTitleAndLink(String title, String link);
}
