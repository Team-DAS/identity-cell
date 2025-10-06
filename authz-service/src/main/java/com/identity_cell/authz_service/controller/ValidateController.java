package com.identity_cell.authz_service.controller;

import com.identity_cell.authz_service.service.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authz")
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        boolean isValid = validateService.validateToken(authHeader);
        return ResponseEntity.ok(isValid);
    }
}
