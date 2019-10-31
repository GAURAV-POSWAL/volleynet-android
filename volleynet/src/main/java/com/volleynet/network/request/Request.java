package com.volleynet.network.request;

/**
 * Implement this interface on all your base request classes
 */
public interface Request {
    /**
     * Adds the request to the request queue for execution.
     */
    void executeRequest();
}
