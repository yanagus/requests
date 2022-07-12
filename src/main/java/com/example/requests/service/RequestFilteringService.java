package com.example.requests.service;

/**
 * Service for filtering requests.
 */
public interface RequestFilteringService {

    /**
     * Allows requests by user IP.
     *
     * @param userIp user IP
     * @return true if request is allowed, false otherwise
     */
    boolean allowRequest(String userIp);
}
