package com.example.requests.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Information about request.
 */
public class RequestInfo {

    /**
     * Time-stamp when first request occurred.
     */
    private Instant firstRequestTime;

    /**
     * The requests count.
     */
    private Long requestsCount;

    public RequestInfo() {
    }

    public RequestInfo(Instant firstRequestTime, Long requestsCount) {
        this.firstRequestTime = firstRequestTime;
        this.requestsCount = requestsCount;
    }

    public Instant getFirstRequestTime() {
        return firstRequestTime;
    }

    public void setFirstRequestTime(Instant firstRequestTime) {
        this.firstRequestTime = firstRequestTime;
    }

    public Long getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(Long requestsCount) {
        this.requestsCount = requestsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestInfo request = (RequestInfo) o;
        return Objects.equals(firstRequestTime, request.firstRequestTime) && Objects.equals(requestsCount, request.requestsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstRequestTime, requestsCount);
    }

    @Override
    public String toString() {
        return "Request{" +
                "firstRequestTime=" + firstRequestTime +
                ", requestsCount=" + requestsCount +
                '}';
    }
}
