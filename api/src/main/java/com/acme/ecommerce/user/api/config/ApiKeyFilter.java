package com.acme.ecommerce.user.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/v3/api-docs",
            "/docs/swagger-ui",
            "/swagger-ui",
            "/webjars",
            "/favicon.ico"
    );

    private static final String UNAUTHORIZED_RESPONSE =
            "{\"status\":{\"code\":\"UNAUTHORIZED\",\"message\":\"Unauthorized: Invalid or missing API key\"},\"data\":null,\"metaData\":{\"requestId\":null}}";

    @Value("${app.api-key}")
    private String configuredApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        if (isExcluded(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || apiKey.isBlank() || !apiKey.equals(configuredApiKey)) {
            log.warn("Unauthorized request to [{}]: invalid or missing API key", requestPath);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(UNAUTHORIZED_RESPONSE);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcluded(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}
