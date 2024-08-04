package com.example.atividade1.security;

// Importações necessárias para a classe
import com.example.atividade1.model.User;
import com.example.atividade1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service // Anotação que indica que esta classe é um serviço gerenciado pelo Spring
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired // Injeção de dependência para acessar o repositório de usuários
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo nome de login
        User user = userRepository.findByLogin(username);

        // Se o usuário não for encontrado, lança uma exceção
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Retorna um objeto UserDetails com as informações do usuário
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), // Nome de usuário
                user.getPassword(), // Senha (geralmente criptografada)
                new ArrayList<>() // Lista de autorizações (roles)
        );
    }
}
