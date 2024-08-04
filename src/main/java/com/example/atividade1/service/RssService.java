package com.example.atividade1.service;

import com.example.atividade1.model.Category;
import com.example.atividade1.model.NewsItem;
import com.example.atividade1.repository.NewsItemRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service // Marca a classe como um serviço gerenciado pelo Spring
public class RssService {

    private final NewsItemRepository newsItemRepository; // Repositório para gerenciar notícias
    private final CategoryService categoryService; // Serviço para gerenciar categorias

    public RssService(NewsItemRepository newsItemRepository, CategoryService categoryService) {
        this.newsItemRepository = newsItemRepository;
        this.categoryService = categoryService;
    }

    // Extrai a URL da imagem a partir da descrição HTML da notícia
    private String extractImageUrl(String description) {
        if (description != null) {
            // Expressão regular para encontrar URLs de imagens na descrição HTML
            Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(description);
            if (matcher.find()) {
                return matcher.group(1); // Retorna a primeira correspondência encontrada
            }
        }
        return null; // Retorna null se não encontrar nenhuma imagem
    }

    @Transactional // Indica que o método deve ser executado dentro de uma transação
    public void updateNewsFromCategory(Category category) throws IOException, FeedException {
        // Lê o feed RSS da URL fornecida pela categoria
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(category.getRssUrl())));
        List<SyndEntry> entries = feed.getEntries(); // Obtém as entradas do feed RSS

        for (SyndEntry entry : entries) {
            String title = entry.getTitle(); // Extrai o título da notícia
            String link = entry.getLink();   // Extrai o link da notícia

            // Verifica se a notícia já existe no banco de dados
            Optional<NewsItem> existingNewsItem = newsItemRepository.findByTitleAndLink(title, link);
            if (existingNewsItem.isPresent()) {
                continue; // Ignora se a notícia já existir
            }

            // Cria um novo objeto NewsItem
            NewsItem newsItem = new NewsItem();
            newsItem.setTitle(title); // Define o título da notícia
            newsItem.setLink(link); // Define o link da notícia
            newsItem.setPublicationDate(entry.getPublishedDate() != null ? entry.getPublishedDate() : new Date()); // Define a data de publicação
            newsItem.setImageUrl(extractImageUrl(entry.getDescription().getValue())); // Define a URL da imagem
            newsItem.setCategory(category); // Define a categoria da notícia

            // Salva a nova notícia no banco de dados
            newsItemRepository.save(newsItem);
        }
    }
}
