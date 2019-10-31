package com.volleynet.delegate;

/**
 * This is a general request handler which can be used at many places where we need a single object or the error.
 */
public interface RequestHandler {
    public void handle(Object object, Exception exception);
}
