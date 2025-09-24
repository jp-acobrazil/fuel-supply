package com.acobrazil.fuelsupply.config;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;

@Component
public class CorsLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CorsLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro, se necessário
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Logando informações de CORS
        logger.info("CORS Request: Method = {}, Origin = {}, Path = {}",
                httpRequest.getMethod(),
                httpRequest.getHeader("Origin"),
                httpRequest.getRequestURI());

        // Logando cookies
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            String cookieString = Arrays.stream(cookies)
                    .map(Cookie::getName)
                    .reduce((c1, c2) -> c1 + ", " + c2)
                    .orElse("No cookies");
            logger.info("Cookies: {}", cookieString);
            logger.info("#################################################################################################################################");
        } else {
            logger.info("#################################################################################################################################");
            logger.info("No cookies present.");
        }

        // Continue a cadeia de filtros
        chain.doFilter(request, response);

        // Logando resposta
        logger.info("CORS Response: Status = {}, Headers = {}",
                httpResponse.getStatus(),
                httpResponse.getHeaderNames());
    }

    @Override
    public void destroy() {
        // Limpeza do filtro, se necessário
    }
}
