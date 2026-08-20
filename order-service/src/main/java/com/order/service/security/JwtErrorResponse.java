package com.order.service.security;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class JwtErrorResponse {

    private LocalDateTime timestamp;
    private String error;
    private String message;
    private int status;
    private String path;
}
