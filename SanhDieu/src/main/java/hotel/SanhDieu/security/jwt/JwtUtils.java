package hotel.SanhDieu.security.jwt;

import hotel.SanhDieu.security.user.HotelUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; //for 7 days
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationTime;
//    private final SecretKey Key;
//
//    public JwtUtils() {
//        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
//        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
//        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
//
//    }
//    public String generateToken(UserDetails userDetails) {
//        return Jwts.builder()
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();
//    }
//    public String extractUsername(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
//        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
//    }
//
//    public boolean isValidToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaims(token, Claims::getExpiration).before(new Date());
//    }

    public String generateJwtTokenForUser(Authentication authentication){
        HotelUserDetails userPrincipal = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles",roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+ EXPIRATION_TIME))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(key()).build().parse(token);
            return true;
        }catch (MalformedJwtException e) {
            logger.error("invalid jwt token: {}", e.getMessage());
        }catch (ExpiredJwtException e) {
            logger.error("Expired token: {}",e.getMessage());
        }catch (UnsupportedJwtException e) {
            logger.error("This token is not supported : {} ",e.getMessage());
        }catch (IllegalArgumentException e) {
            logger.error("No claims found: {}",e.getMessage());
        }
        return false;
    }

}
