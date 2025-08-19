package com.barcazone.dto;

import lombok.Data;

@Data public class AuthResponse {
    private String accessToken; private long expiresIn;
    private String tokenType = "Bearer"; }
