/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.authguard.authguard_oauth2_service.Exception;


public class ResourceException extends Exception {
    String msg;
    
    public ResourceException(String msg){
        super(msg);
    }
}
