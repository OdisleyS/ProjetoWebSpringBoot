package com.example.atividade1.controller;

// Importações necessárias para a classe
import com.example.atividade1.model.Category;
import com.example.atividade1.model.NewsItem;
import com.example.atividade1.model.User;
import com.example.atividade1.repository.CategoryRepository;
import com.example.atividade1.repository.NewsItemRepository;
import com.example.atividade1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController // Indica que a classe é um controlador REST, lidando com requisições HTTP
@RequestMapping("/api/user") // Define o prefixo da URL para os endpoints deste controlador
public class UserController {

    // Injeção de dependências para interagir com o banco de dados
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsItemRepository newsItemRepository;

    // Endpoint para DEFINIR categorias favoritas do usuário
    @PostMapping("/favorites")
    public ResponseEntity<?> setFavoriteCategories(@RequestBody Set<Long> categoryIds) {
        // Obtém o nome de usuário atual do contexto de segurança
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário pelo nome de login
        User user = userRepository.findByLogin(currentUsername);

        // Verifica se o usuário foi encontrado
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Cria um conjunto para armazenar as categorias favoritas
        Set<Category> favoriteCategories = new HashSet<>();
        // Itera sobre os IDs das categorias recebidas
        for (Long categoryId : categoryIds) {
            // Busca a categoria pelo ID e, se encontrada, adiciona ao conjunto
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            categoryOptional.ifPresent(favoriteCategories::add);
        }

        // Define as categorias favoritas do usuário e salva no banco de dados
        user.setFavoriteCategories(favoriteCategories);
        userRepository.save(user);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Favorite categories updated successfully");
    }

    // Endpoint para OBTER categorias favoritas do usuário
    @GetMapping("/favorites")
    public ResponseEntity<?> getFavoriteCategories() {
        // Obtém o nome de usuário atual do contexto de segurança
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário pelo nome de login
        User user = userRepository.findByLogin(currentUsername);

        // Verifica se o usuário foi encontrado
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Obtém as categorias favoritas do usuário
        Set<Category> favoriteCategories = user.getFavoriteCategories();
        // Retorna as categorias favoritas
        return ResponseEntity.ok(favoriteCategories);
    }

    // Endpoint para obter notícias com base nas categorias favoritas do usuário
    @GetMapping("/news")
    public ResponseEntity<?> getNewsByFavorites() {
        // Obtém o nome de usuário atual do contexto de segurança
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Busca o usuário pelo nome de login
        User user = userRepository.findByLogin(currentUsername);

        // Verifica se o usuário foi encontrado
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Obtém as categorias favoritas do usuário
        Set<Category> favoriteCategories = user.getFavoriteCategories();
        List<NewsItem> newsItems;

        // Se o usuário não tiver categorias favoritas, busca todas as notícias
        if (favoriteCategories.isEmpty()) {
            newsItems = newsItemRepository.findAll();
        } else {
            // Caso contrário, busca notícias que pertençam às categorias favoritas
            newsItems = newsItemRepository.findAllByCategoryIn(favoriteCategories);
        }

        // Retorna as notícias encontradas
        return ResponseEntity.ok(newsItems);
    }
}
