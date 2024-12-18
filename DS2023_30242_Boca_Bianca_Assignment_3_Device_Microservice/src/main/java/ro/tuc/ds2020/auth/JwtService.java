package ro.tuc.ds2020.auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.PersonTabel;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractRole(String token) {
        Map<String, Object> claims = extractAllClaims(token);
        return claims.get("role").toString();
    }

    public UUID extractId(String token) {
        Map<String, Object> claims = extractAllClaims(token);
   //    System.out.println(UUID.fromString(claims.get("userId").toString())+"Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
       /// System.out.println(claims.get("role").toString());
        return UUID.fromString(claims.get("userId").toString());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, PersonTabel person) {
        final UUID id = extractId(token);
        return (id.equals(person.getId())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
