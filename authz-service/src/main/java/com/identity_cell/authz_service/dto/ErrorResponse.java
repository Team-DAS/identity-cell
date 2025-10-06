package com.identity_cell.authz_service.dto;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int Status, String error, String message){
}
