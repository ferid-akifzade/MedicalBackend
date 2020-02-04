package org.medical.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@PropertySource("classpath:jwt.properties")
public class JwtAuthService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration.normal}")
    private Long normal;
    @Value("${jwt.expiration.remember}")
    private Long remember;

    public String generateToken(Long id, boolean remember) {
        final Date date = new Date();
        final Date nowDate = new Date(date.getTime() + (remember ? this.remember : this.normal));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject(id.toString())
                .setIssuedAt(date)
                .setExpiration(nowDate)
                .compact();
    }

    public Long extractIdFromClaims(Jws<Claims> claimsJws) {
        return Long.parseLong(claimsJws.getBody().getSubject());
    }

    public Optional<Jws<Claims>> tokenToClaims(String token) {
        try {
        return Optional.of(
                Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token));
        }catch (SignatureException |
                MalformedJwtException |
                IllegalArgumentException |
                UnsupportedJwtException |
                ExpiredJwtException ex) {
            log.info(ex.getMessage());
        }
        return Optional.empty();
    }

}
