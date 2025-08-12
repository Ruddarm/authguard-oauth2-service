package com.authguard.authguard_oauth2_service.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizeCodePayload implements  Serializable {
    private String userId;
    private String client_Id;
    private UUID appUserLinkId;
    private String nonce;
}
