package com.authguard.authguard_oauth2_service.services;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.authguard.authguard_oauth2_service.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SecretJwtKey = "ddgdbydjsmsjjsmhdgdndjsksjbdddjdkddk";
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtService() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            this.publicKey = (RSAPublicKey) keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
    }

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(SecretJwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createOauth2AccessToken(String userId, String clientId) {
        return Jwts.builder().subject("userId").claim("client_id", clientId).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .signWith(generateSecretKey())
                .compact();
    }

    public String createreOauth2refreshToken(String userId, String clientId) {
        return Jwts.builder().subject("userId").claim("client_id", clientId).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 30))
                .signWith(generateSecretKey())
                .compact();
    }

    public String createIdToken(String nonce, User user, String clientId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .issuer("http://localhost:8081")
                .subject(user.getUserId().toString())
                .claim("aud", clientId).claim("nonce", nonce)
                .claim("email", user.getEmail()).claim("name", user.getFirstName() + " " + user.getLastName())
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000 * 60 * 5))
                .signWith(privateKey, Jwts.SIG.RS256) // ✅ This is the modern way
                .compact();

    }

}
