package com.example.atividade1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta classe fornece configurações de segurança
@EnableWebSecurity // Habilita as configurações de segurança para a aplicação web
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filtro para autenticação com JWT

    // Construtor que injeta o filtro de autenticação JWT
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean // Define um bean para configurar o filtro de segurança da aplicação
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF, pois o JWT é usado para segurança
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/public/**").permitAll() // Permite acesso público a URLs que começam com /api/public/
                        .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Define que a aplicação não usará sessões de servidor
                );

        // Adiciona o filtro de autenticação JWT antes do filtro padrão de autenticação com nome e senha
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Constrói e retorna a configuração de segurança
    }

    @Bean // Define um bean para o codificador de senhas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa o BCrypt para codificar senhas de forma segura
    }

    @Bean // Define um bean para o gerenciador de autenticação
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Obtém o gerenciador de autenticação da configuração
    }
}
