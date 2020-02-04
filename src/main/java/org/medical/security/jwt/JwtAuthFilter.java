package org.medical.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.medical.repositories.OrdinaryUserRepo;
import org.medical.security.seclib.OrdinaryUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthService authService;
    private final OrdinaryUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtAuthService authService, OrdinaryUserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            extractTokenFromRequest(request)
                .flatMap(authService::tokenToClaims)
                    .map(authService::extractIdFromClaims)
                    .map(userDetailsService::loadUserById)
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities()))
                    .ifPresent(usernamePasswordAuthenticationToken -> {
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    });
            filterChain.doFilter(request,response);
        }catch (Exception ex){
            logger.info(ex.getMessage());
        }
    }

    public Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        final String auth_head = request.getHeader(Const.AUTH_HEADER);
        if (StringUtils.hasText(auth_head) && auth_head.startsWith(Const.AUTH_HEADER)) {
            return Optional.of(auth_head.substring(Const.AUTH_BEARER.length()));
        }
        final String param = request.getParameter(Const.AUTH_TOKEN);
        if (!StringUtils.isEmpty(param)) {
            return Optional.of(param.substring(Const.AUTH_BEARER.length()));
        }
        return Optional.empty();
    }

}
