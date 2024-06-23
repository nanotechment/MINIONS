package com.example.minions.util;

import com.example.minions.entity.Account;
import com.example.minions.model.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${JWT_SECRET_KEY}")
    private String secret;
    @Value("86400000")
    private long jwtExpiration;
    @Value("604800000")
    private long refreshExpiration;
    /*
     * Function-name: Generate Token after login
     * Author: Hoàng Đinh
     * Date: 29/09/2023
     */
    public String generateToken(TokenPayload tokenPayload, long expiredDate){
        Map<String, Object> claims = new HashMap<>();
        claims.put("payload", tokenPayload);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredDate*1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
    }
    public String generateRefToken(TokenPayload tokenPayload){
        Map<String, Object> claims = new HashMap<>();
        claims.put("payload", tokenPayload);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
    }
    /*
     * Function-name: get secret key for generate token
     * Author: Hoàng Đinh
     * Date: 29/09/2023
     */
    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /*
     * Function-name: get payload part in token
     * Author: Hoàng Đinh
     * Date: 29/09/2023
     */

    public TokenPayload getTokenPayLoad(String token) {
        return getClaimFromToken(token, (Claims claim) -> {
            Map<String, Object> mapResult = (Map<String, Object>) claim.get("payload");
            return TokenPayload.builder()
                    .accountId((String)mapResult.get("accountId"))
                    .username((String) mapResult.get("username"))
                    .build();
        });
    }
    /*
     * Function-name: get claim part in token
     * Author: Hoàng Đinh
     * Date: 29/09/2023
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secret.getBytes())
                .build().parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);


    }
    /*
     * Function-name: check token valid
     * Author: Hoàng Đinh
     * Date: 29/09/2023
     */
    public boolean isValid(String token, TokenPayload tokenPayFromAccount){
        if(isTokenExpired(token)){
            return  false;
        }
        TokenPayload tokenPayload = getTokenPayLoad(token);
        return tokenPayload.getAccountId().equals(tokenPayFromAccount.getAccountId()) && tokenPayload.getUsername().equals(tokenPayFromAccount.getUsername());
    }
    /*
     * Function-name: check token expired
     * Author: Hoàng Đinh
     * Date: 29/09/2023
     */
    private boolean isTokenExpired(String token){
        Date expiredTime = getClaimFromToken(token, Claims::getExpiration);
        return expiredTime.before(new Date());
    }


    public boolean isTokenValid(String refreshToken, Account userDetails) {
        final String username = extractUsername(refreshToken);
        return (username.equals(userDetails.getAccountId())) && !isTokenExpired(refreshToken);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Object generateReToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
