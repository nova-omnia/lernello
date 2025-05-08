package ch.nova_omnia.lernello.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ch.nova_omnia.lernello.repository.UserRepository;
import ch.nova_omnia.lernello.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for handling JWT tokens and authenticating users each request.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Filters the request and authenticates the user if a JWT token is present.
     * If the token is valid, the user is authenticated and the request is continued.
     *
     * @param request     The request to filter.
     * @param response    The response to filter.
     * @param filterChain The filter chain to continue the request.
     * @throws ServletException If an error occurs during the filter.
     * @throws IOException      If an error occurs during the filter.
     */
    @Override
    protected void doFilterInternal(
                                    HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                UUID uuid = jwtUtils.getUserIdFromToken(jwt);
                String username = userRepository.findByUuid(uuid).getUsername();

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lernello_auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
