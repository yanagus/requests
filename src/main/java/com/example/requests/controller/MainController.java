package com.example.requests.controller;

import com.example.requests.service.RequestFilteringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Main controller
 */
@RestController
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final RequestFilteringService service;

    @Autowired
    public MainController(RequestFilteringService service) {
        this.service = service;
    }

    @GetMapping("/api")
    public ResponseEntity<Void> getResponse(HttpServletRequest request) {
        String userIp = request.getRemoteAddr();
        log.info("Received request from ip {}", userIp);
        if (service.allowRequest(userIp)) {
            log.info("Request is allowed for ip {}", userIp);
            return ResponseEntity.ok().build();
        } else {
            log.info("Request is not allowed for ip {}", userIp);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
}
