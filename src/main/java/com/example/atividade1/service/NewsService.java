package com.example.atividade1.service;

import com.example.atividade1.model.Category;
import com.example.atividade1.model.NewsItem;
import com.example.atividade1.repository.CategoryRepository;
import com.example.atividade1.repository.NewsItemRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service // Marca a classe como um serviço gerenciado pelo Spring
public class NewsService {

    private final NewsItemRepository newsItemRepository; // Repositório para gerenciar notícias
    private final CategoryRepository categoryRepository; // Repositório para gerenciar categorias

    @Autowired
    public NewsService(NewsItemRepository newsItemRepository, CategoryRepository categoryRepository) {
        this.newsItemRepository = newsItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional // Indica que o método deve ser executado em uma transação
    public void updateNewsForAllCategories() throws IOException, FeedException {
        List<Category> categories = categoryRepository.findAll(); // Obtém todas as categorias do banco de dados
        for (Category category : categories) {
            updateNewsFromCategory(category); // Atualiza as notícias para cada categoria
        }
    }

    // Atualiza as notícias para uma categoria específica
    private void updateNewsFromCategory(Category category) throws IOException, FeedException {
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(category.getRssUrl()))); // Lê o feed RSS da URL da categoria
        List<SyndEntry> entries = feed.getEntries(); // Obtém as entradas do feed RSS
        for (SyndEntry entry : entries) {
            NewsItem newsItem = new NewsItem();
            newsItem.setTitle(entry.getTitle()); // Define o título da notícia
            newsItem.setLink(entry.getLink()); // Define o link da notícia
            newsItem.setPublicationDate(entry.getPublishedDate() != null ? entry.getPublishedDate() : new Date()); // Define a data de publicação
            newsItem.setImageUrl(entry.getDescription().getValue()); // Define a URL da imagem (simplificado, precisa de parsing)
            newsItem.setCategory(category); // Define a categoria da notícia
            newsItemRepository.save(newsItem); // Salva a notícia no banco de dados
        }
    }

    // Recupera todas as notícias associadas a uma categoria específica identificada pelo ID
    public List<NewsItem> getNewsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId)); // Lança exceção se a categoria não for encontrada
        return newsItemRepository.findAllByCategory(category); // Retorna todas as notícias para a categoria
    }

    // Recupera todas as notícias armazenadas no banco de dados
    public List<NewsItem> getAllNews() {
        return newsItemRepository.findAll(); // Retorna todas as notícias
    }
}
