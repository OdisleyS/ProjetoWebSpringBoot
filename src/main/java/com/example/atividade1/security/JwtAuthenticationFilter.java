package com.example.atividade1.security;

// Importações necessárias
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component // Anotação que define a classe como um componente gerenciado pelo Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired // Injeção de dependência do provedor de token JWT
    private JwtTokenProvider jwtTokenProvider;

    @Autowired // Injeção de dependência do serviço de detalhes do usuário personalizado
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Extrai o token JWT da requisição
        String token = getJwtFromRequest(request);

        // Verifica se o token é válido
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Obtém o nome de usuário do token JWT
            String username = jwtTokenProvider.getUsernameFromJWT(token);

            // Carrega os detalhes do usuário com base no nome de usuário
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Cria um objeto de autenticação com os detalhes do usuário
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Define a autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua com o próximo filtro na cadeia
        chain.doFilter(request, response);
    }

    // Método para extrair o token JWT da requisição
    private String getJwtFromRequest(HttpServletRequest request) {
        // Obtém o cabeçalho de autorização da requisição
        String bearerToken = request.getHeader("Authorization");
        // Verifica se o cabeçalho começa com "Bearer " e extrai o token
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " para obter o token real
        }
        return null; // Retorna null se não houver token
    }
}
