package com.authguard.authguard_oauth2_service.dtos;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing application details returned in API responses.
 *
 * <p>Contains basic information about an application such as
 * its name, client ID, user ID, and optionally the client secret.</p>
 *
 * <p>Implements {@link Serializable} to allow this object to be
 * safely transmitted over the network or stored in distributed caches.</p>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse implements Serializable {

    /** Name of the application */
    private String appName;

    /** Unique client identifier for the application */
    private UUID client_Id;

    /** ID of the user who owns the application */
    private UUID userId;

    /** Secret key associated with the application client (sensitive) */
    private String client_secret;

    /**
     * Convenience constructor for creating an AppResponse without a client secret.
     *
     * @param appName   name of the application
     * @param client_Id unique client ID
     * @param userId    ID of the owner user
     */
    public AppResponse(String appName, UUID client_Id, UUID userId) {
        this.appName = appName;
        this.client_Id = client_Id;
        this.userId = userId;
    }
}
