package com.authguard.authguard_oauth2_service.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class User implements   Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String userId;
    private String password;
    private String email;
}
