package com.example.atividade1.controller;

// Importações necessárias para a classe
import com.example.atividade1.model.User;
import com.example.atividade1.repository.UserRepository;
import com.example.atividade1.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que esta classe é um controlador REST, que processa requisições HTTP
@RequestMapping("/api/public") // Define o prefixo das URLs que esta classe vai tratar
public class AuthController {

    // Injeta automaticamente as dependências necessárias
    @Autowired
    private UserRepository userRepository; // Repositório para interagir com a tabela de usuários no banco de dados

    @Autowired
    private PasswordEncoder passwordEncoder; // Utilizado para codificar senhas

    @Autowired
    private JwtTokenProvider tokenProvider; // Provê tokens JWT para autenticação

    @Autowired
    private AuthenticationManager authenticationManager; // Gerencia o processo de autenticação dos usuários

    // Endpoint para registrar novos usuários
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Codifica a senha do usuário antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Define o status do usuário como ATIVO
        user.setStatus(User.Status.ACTIVE);
        // Salva o usuário no banco de dados
        userRepository.save(user);
        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("User registered successfully");
    }

    // Endpoint para login de usuários
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        // Busca o usuário no banco de dados usando o e-mail fornecido
        User user = userRepository.findByEmail(email);
        // Verifica se o usuário existe e se a senha fornecida é válida
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Cria um token de autenticação usando o gerenciador de autenticação
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(), // Nome de usuário
                            password // Senha
                    )
            );
            // Gera um token JWT para o usuário autenticado
            String token = tokenProvider.createToken(authentication);
            // Retorna o token JWT como resposta
            return ResponseEntity.ok(token);
        }
        // Retorna uma resposta de erro caso o e-mail ou senha sejam inválidos
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email/password supplied");
    }
}
