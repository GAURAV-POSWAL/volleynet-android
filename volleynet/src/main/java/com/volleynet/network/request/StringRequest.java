package com.volleynet.network.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.volleynet.network.RequestManager;
import com.volleynet.network.params.RequestParams;

import java.util.HashMap;
import java.util.Map;

public class StringRequest extends com.android.volley.toolbox.StringRequest implements Request {
    private RequestParams requestParams;

    /**
     * Creates a new request with the given method.
     *
     * @param requestParams The request parameter
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public StringRequest(RequestParams requestParams, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(requestParams.getMethod(), requestParams.getUrl(), listener, errorListener);
        this.requestParams = requestParams;
        // Setting the tag for the request
        this.setTag(requestParams.getTag());
        setShouldCache(false);
    }

    //Getter
    public RequestParams getRequestParams() {
        return requestParams;
    }

    //Setter
    public void setRequestParams(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return requestParams.getHeaders() != null ? requestParams.getHeaders() : new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return requestParams.getParameters();
    }

    @Override
    public void executeRequest() {
        RequestManager.getInstance().addToRequestQueue(this);
    }
}
