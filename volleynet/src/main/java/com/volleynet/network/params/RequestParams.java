package com.volleynet.network.params;

import java.util.HashMap;

public class RequestParams {
    protected String url;
    protected int method;
    protected HashMap<String, String> headers;
    protected HashMap<String, String> parameters;
    // used for volley request, in case you want to cancel it.
    protected Object tag;

    /**
     * Protected constructor for subclasses
     */
    protected RequestParams() {
    }

    /**
     * Constructor to initialize all ivars
     *
     * @param method     http method e.g. Request.Method.GET
     * @param url        Service url
     * @param headers    Headers
     * @param parameters Parameters
     */
    public RequestParams(int method, String url, HashMap<String, String> headers, HashMap<String, String> parameters, Object tag) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.parameters = parameters;
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public int getMethod() {
        return method;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public Object getTag() {
        return this.tag;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }
}
