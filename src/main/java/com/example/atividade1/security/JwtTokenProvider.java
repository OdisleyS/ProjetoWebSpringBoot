package com.example.atividade1.security;

// Importações necessárias
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // Indica que a classe é um componente gerenciado pelo Spring
public class JwtTokenProvider {

    @Value("${jwt.secret}") // Injeta o valor da propriedade jwt.secret
    private String jwtSecret;

    @Value("${jwt.expiration-in-ms}") // Injeta o valor da propriedade jwt.expiration-in-ms
    private int jwtExpirationInMs;

    // Método para criar um token JWT
    public String createToken(Authentication authentication) {
        // Obtém o nome de usuário da autenticação
        String username = authentication.getName();

        // Define a data atual e a data de expiração do token
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Constrói e retorna o token JWT
        return Jwts.builder()
                .setSubject(username) // Define o nome de usuário como assunto
                .setIssuedAt(new Date()) // Define a data de emissão
                .setExpiration(expiryDate) // Define a data de expiração
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Assina o token com a chave secreta
                .compact();
    }

    // Método para obter o nome de usuário de um token JWT
    public String getUsernameFromJWT(String token) {
        // Analisa o token e extrai as claims
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret) // Define a chave secreta para validar o token
                .parseClaimsJws(token) // Analisa o token
                .getBody(); // Obtém o corpo do token

        return claims.getSubject(); // Retorna o nome de usuário do assunto
    }

    // Método para validar um token JWT
    public boolean validateToken(String token) {
        try {
            // Tenta analisar o token com a chave secreta
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true; // Retorna verdadeiro se o token for válido
        } catch (Exception ex) {
            // Captura exceções se o token for inválido ou expirado
            // Pode adicionar mais logs ou manipulação de exceções
        }
        return false; // Retorna falso se o token não for válido
    }
}
