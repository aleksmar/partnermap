package com.example.partnermap.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.partnermap.security.SecurityConstants.HEADER;
import static com.example.partnermap.security.SecurityConstants.TOKEN_PREFIX;

public class CustomerUrlFilter extends BasicAuthenticationFilter {
    public CustomerUrlFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(HEADER);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String uri = request.getRequestURI();
        Matcher matcher = Pattern.compile("/customers/(@me|\\w+)(/.*)?").matcher(uri);
        if (!matcher.find()) {
            chain.doFilter(request, response);
            return;
        }

        String urlId = matcher.group(1);
        String authId = token.replaceFirst(TOKEN_PREFIX, "");
        if ("@me".equals(urlId) || Objects.equals(authId, urlId)) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authId, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}
