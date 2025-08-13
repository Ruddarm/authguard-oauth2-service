package com.authguard.authguard_oauth2_service.services;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.authguard.authguard_oauth2_service.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SecretJwtKey = "ddgdbydjsmsjjsmhdgdndjsksjbdddjdkddk";
    @Value("${jwt.privateKey}")
    private String privateKey;
    @Value("${jwt.publicKey}")
    private String publicKey;
    @Value("${spring.application.name}")
    private String serviceId;

    // public JwtService() {
    // try {
    // KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    // keyPairGenerator.initialize(2048);
    // KeyPair keyPair = keyPairGenerator.generateKeyPair();
    // this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    // this.publicKey = (RSAPublicKey) keyPair.getPublic();
    // } catch (NoSuchAlgorithmException e) {
    // throw new RuntimeException("Error generating RSA key pair", e);
    // }
    // }
    private RSAPrivateKey getPrivateKey() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        return (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private RSAPublicKey getPublicKey() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(decoded));
    }

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(SecretJwtKey.getBytes(StandardCharsets.UTF_8));
    }

    /*
     * Create oauth2AccessToken will required in future for api access
     * 
     * @param userid and clientid is requreid
     * 
     * @return jwt accesstoken of type string.
     */
    public String createOauth2AccessToken(String userId, String clientId) {
        return Jwts.builder().subject("userId").claim("client_id", clientId).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .signWith(generateSecretKey())
                .compact();
    }

    /*
     * Create oauth2AccessToken will required in future for api access
     * 
     * @param userid and clientid is requreid
     * 
     * @return jwt accesstoken of type string.
     */
    public String createreOauth2refreshToken(String userId, String clientId) {
        return Jwts.builder().subject("userId").claim("client_id", clientId).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .signWith(generateSecretKey())
                .compact();
    }

    /*
     * Create id token to get detials of user token claims
     * 
     * @param userid and clientid and nonce is requreid
     * 
     * @return jwt accesstoken of type string.
     */
    public String createIdToken(String nonce, User user, String clientId) throws InvalidKeyException, Exception {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .issuer("http://localhost:8081")
                .subject(user.getUserId().toString())
                .claim("aud", clientId).claim("nonce", nonce)
                .claim("email", user.getEmail()).claim("name", user.getFirstName() + " " + user.getLastName())
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000 * 60 * 5))
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    /*
     * Create a acesstoken for inter microservice communication
     * 
     * @param Hashmap<String,Object> a map of all calims
     * 
     * @param String subject
     * 
     * @return access token sing with private key
     */
    public String serviceAccessToken(Map<String, Object> claimsMap)
            throws InvalidKeyException, Exception {
        long now = System.currentTimeMillis();
        return Jwts.builder().issuer("authguard-service")
                .subject(serviceId)
                .claims(claimsMap)
                .issuedAt(new Date(now))
                .expiration(new Date(now + 5 * 60 * 1000))
                .signWith(getPrivateKey(), Jwts.SIG.RS256).compact();
    }

    /*
     * Verify service access token inter microservice communication
     * 
     * @param String token singed with private key
     * 
     * @return Claims all claims.
     */
    public Claims verifyServiceToken(String token) throws JwtException, IllegalArgumentException, Exception {
        Claims claims = Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token).getPayload();
        return claims;
    }

}
