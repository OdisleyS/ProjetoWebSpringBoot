package com.example.atividade1.service;

import com.example.atividade1.model.Category;
import com.example.atividade1.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service // Marca a classe como um serviço gerenciado pelo Spring
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository; // Repositório para acessar o banco de dados de categorias

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class); // Logger para registrar informações e erros

    @PostConstruct // Anotação que indica que este método deve ser executado após a construção do bean
    public void init() {
        // Log para iniciar a inicialização das categorias
        logger.info("Checking and updating categories as necessary...");

        // Adiciona ou atualiza categorias com URLs de feeds RSS
        addOrUpdateCategory("Carros", "https://g1.globo.com/dynamo/carros/rss2.xml");
        addOrUpdateCategory("Ciência e Saúde", "https://g1.globo.com/dynamo/ciencia-e-saude/rss2.xml");
        addOrUpdateCategory("Concursos e Emprego", "https://g1.globo.com/dynamo/concursos-e-emprego/rss2.xml");
        addOrUpdateCategory("Economia", "https://g1.globo.com/dynamo/economia/rss2.xml");
        addOrUpdateCategory("Educação", "https://g1.globo.com/dynamo/educacao/rss2.xml");
        addOrUpdateCategory("Loterias", "https://g1.globo.com/dynamo/loterias/rss2.xml");
        addOrUpdateCategory("Mundo", "https://g1.globo.com/dynamo/mundo/rss2.xml");
        addOrUpdateCategory("Música", "https://g1.globo.com/dynamo/musica/rss2.xml");
        addOrUpdateCategory("Natureza", "https://g1.globo.com/dynamo/natureza/rss2.xml");
        addOrUpdateCategory("Planeta Bizarro", "https://g1.globo.com/dynamo/planeta-bizarro/rss2.xml");
        addOrUpdateCategory("Política", "https://g1.globo.com/dynamo/politica/mensalao/rss2.xml");
        addOrUpdateCategory("Pop & Arte", "https://g1.globo.com/dynamo/pop-arte/rss2.xml");
        addOrUpdateCategory("Tecnologia e Games", "https://g1.globo.com/dynamo/tecnologia/rss2.xml");
        addOrUpdateCategory("Turismo e Viagem", "https://g1.globo.com/dynamo/turismo-e-viagem/rss2.xml");
    }

    // Adiciona uma nova categoria ou atualiza uma existente com uma nova URL de feed RSS
    private void addOrUpdateCategory(String name, String url) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name); // Procura a categoria pelo nome
        Category category;
        if (!optionalCategory.isPresent()) {
            // Se a categoria não existir, cria uma nova
            category = new Category(name, url);
            logger.info("Creating new category: {}", name); // Log para registrar a criação da categoria
        } else {
            // Se a categoria existir, atualiza a URL do feed RSS
            category = optionalCategory.get();
            category.setRssUrl(url);
            logger.info("Updating existing category: {}", name); // Log para registrar a atualização da categoria
        }
        categoryRepository.save(category); // Salva a categoria no banco de dados
    }

    // Retorna uma lista de todas as categorias armazenadas no banco de dados
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Salva ou atualiza uma categoria no banco de dados
    public Category saveOrUpdateCategory(Category category) {
        return categoryRepository.save(category); // Salva a categoria e retorna a instância salva
    }
}
