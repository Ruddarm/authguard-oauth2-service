package com.authguard.authguard_oauth2_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Data tranfer object representing Error response 
 * return by API if any error occured
 * 
 * <p>
 *  This object is return by the exceptin handler to provide 
 *  Error in human readable format
 * </P>
 * 
 * Example JSON:
 * {
 *  "message":"Invalid Format" 
 * }
 * 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    String message;
}
