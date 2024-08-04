package com.example.atividade1.scheduler;

import com.example.atividade1.service.RssService;
import com.example.atividade1.model.Category;
import com.example.atividade1.service.CategoryService;
import com.rometools.rome.io.FeedException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component // Marca a classe como um componente gerenciado pelo Spring
public class RssScheduler {

    private final RssService rssService; // Serviço para lidar com feeds RSS
    private final CategoryService categoryService; // Serviço para gerenciar categorias

    // Construtor que injeta os serviços necessários
    public RssScheduler(RssService rssService, CategoryService categoryService) {
        this.rssService = rssService;
        this.categoryService = categoryService;
    }

    // Define que este método deve ser executado a cada 1 hora (3600000 milissegundos)
    @Scheduled(fixedRate = 3600000)
    public void fetchNews() {
        try {
            List<Category> categories = categoryService.findAll(); // Obtém todas as categorias de notícias
            for (Category category : categories) {
                rssService.updateNewsFromCategory(category); // Atualiza as notícias para cada categoria
            }
        } catch (IOException | FeedException e) {
            e.printStackTrace(); // Imprime o stack trace se ocorrer um erro ao buscar ou processar as notícias
        }
    }
}
