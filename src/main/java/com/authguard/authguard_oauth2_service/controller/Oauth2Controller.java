package com.authguard.authguard_oauth2_service.controller;

import java.util.Base64;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.authguard.authguard_oauth2_service.services.OAuth2Service;

import io.jsonwebtoken.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/name")
    public String getMethodName() {
        return "done";
    }

    @PostMapping("/token")
    public ResponseEntity<?> exchangeTokenCode(@RequestParam Map<String, String> params,
            @RequestHeader HttpHeaders headers) throws InvalidKeyException, Exception{

        String grantType = params.get("grant_type");
        String code = params.get("code");
        String redirectUri = params.get("redirect_uri");
        String authHeader = headers.getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic")) {
            return ResponseEntity.badRequest().body(Map.of("errors", "missing_authorization header"));
        }
        String base64Credentials = authHeader.substring("Basic ".length());
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String[] clientCredentials = new String(decoded).split(":", 2);
        if (clientCredentials.length != 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_basic_auth_format"));
        }
        String client_id = clientCredentials[0];
        String clientSecret = clientCredentials[1];
        // UUID client_id;
        // try {
        //     client_id = UUID.fromString(clientId);
        // } catch (IllegalArgumentException e) {
        //     System.out.println("Invalid client Id");
        //     return ResponseEntity.badRequest().body(Map.of("error", "invalid_client_id_format"));
        // }
        if (!"authorization_code".equals(grantType)) {
            System.out.println("Invalid grant type");
            return ResponseEntity.badRequest().body(Map.of("error", "unsupported_grant_type"));
        }
        String[] tokens = oAuth2Service.validateCode(client_id, code, clientSecret);
        // System.out.println("fuking ok");
        return ResponseEntity.ok(Map.of(
                "access_token", tokens[0],
                "token_type", "Bearer",
                "expires_in", 3600,
                "refresh_token", tokens[1],
                "id_token", tokens[2],
                "scope", "read write"));
    }

    @GetMapping("/authorize")
    public RedirectView loginRedirec(@RequestParam String client_id, @RequestParam String redirect_uri,
            @RequestParam String state, @RequestParam String nonce) {
        //TODO  validate url origin
        String loginUrl = "http://localhost:5173/oauth/user/login" + "?client_id=" + client_id +
                "&redirectUrl=" + redirect_uri + "&state=" + state + "&nonce=" + nonce;
        return new RedirectView(loginUrl);
    }

}
