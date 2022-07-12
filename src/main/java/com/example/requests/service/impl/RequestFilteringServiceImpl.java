package com.example.requests.service.impl;

import com.example.requests.model.RequestInfo;
import com.example.requests.service.RequestFilteringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service
public class RequestFilteringServiceImpl implements RequestFilteringService {

    private static final Logger log = LoggerFactory.getLogger(RequestFilteringServiceImpl.class);

    private static final Map<String, RequestInfo> USER_REQUESTS = Collections.synchronizedMap(new HashMap<>());

    private final int amount;
    private final int minutes;

    public RequestFilteringServiceImpl(@Value("${request.restriction.amount}") int amount,
                                       @Value("${request.restriction.minutes}") int minutes) {
        this.amount = amount;
        this.minutes = minutes;
    }

    /**
     * Checks if the request is allowed to execute for user IP by
     * the requests amount per time interval in minutes.
     *
     * @param userIp user IP
     * @return true if request is allowed, false otherwise
     */
    @Override
    public synchronized boolean allowRequest(String userIp) {
        if (!USER_REQUESTS.containsKey(userIp)) {
            USER_REQUESTS.put(userIp, new RequestInfo(Instant.now(), 1L));
            log.info("Requests count {} for userIp {}", 1, userIp);
            return true;
        } else {
            RequestInfo request = USER_REQUESTS.get(userIp);
            if (Instant.now().minusSeconds((long) minutes*60).isAfter(request.getFirstRequestTime())) {
                USER_REQUESTS.replace(userIp, new RequestInfo(Instant.now(), 1L));
                log.info("Requests count {} for userIp {}", 1, userIp);
                return true;
            } else {
                Long count = request.getRequestsCount();
                request.setRequestsCount(++count);
                USER_REQUESTS.replace(userIp, request);
                log.info("Requests count {} for userIp {}", count, userIp);
                return count <= amount;
            }
        }
    }
}
