package com.example.atividade1.controller;

// Importações necessárias para a classe
import com.example.atividade1.model.NewsItem;
import com.example.atividade1.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.atividade1.repository.NewsItemRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController // Indica que a classe é um controlador REST, lidando com requisições HTTP
@RequestMapping("/news") // Define o prefixo da URL para os endpoints deste controlador
public class NewsController {

    @Autowired
    private NewsItemRepository newsItemRepository; // Repositório para interagir com o banco de dados de notícias

    @Autowired
    private final NewsService newsService; // Serviço para lógica de negócios relacionada a notícias

    // Construtor para injetar o NewsService
    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Endpoint para obter as notícias mais recentes
    @GetMapping("/recent")
    public ResponseEntity<List<NewsItem>> getRecentNews() {
        // Busca todas as notícias ordenadas pela data de publicação de forma decrescente
        List<NewsItem> newsItems = newsItemRepository.findAllOrderByPublicationDateDesc();
        // Retorna a lista de notícias em um ResponseEntity com status HTTP 200 (OK)
        return ResponseEntity.ok(newsItems);
    }

    // Endpoint para obter notícias por categoria
    @GetMapping("/category/{categoryId}")
    public List<NewsItem> getNewsByCategory(@PathVariable Long categoryId) {
        // Retorna a lista de notícias para a categoria especificada
        return newsService.getNewsByCategoryId(categoryId);
    }

    // Endpoint para obter todas as notícias
    @GetMapping("/all")
    public List<NewsItem> getAllNews() {
        // Retorna todas as notícias disponíveis
        return newsService.getAllNews();
    }
}
