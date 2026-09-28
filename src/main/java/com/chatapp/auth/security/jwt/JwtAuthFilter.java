package com.chatapp.auth.security.jwt;

import com.chatapp.auth.users.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader=request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        final String token=authorizationHeader.substring(7);
        final String username=jwtService.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            userRepository.findByUsername(username).ifPresent(user -> {
                if (jwtService.isValidToken(token, user)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                            username,null,user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            });

            filterChain.doFilter(request,response);
        }

    }
}
