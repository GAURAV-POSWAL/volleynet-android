package com.volleynet.network.request;

import com.android.volley.Response;
import com.volleynet.network.params.RequestParams;
import com.volleynet.utils.AppLog;
import com.volleynet.utils.Utility;

public class StringGetParamRequest extends StringRequest {

    /**
     * Creates a new request with the given method.
     *
     * @param requestParam  RequestParams required in the method
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    private StringGetParamRequest(RequestParams requestParam, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(requestParam, listener, errorListener);
    }

    /**
     * Creates a new request with the given method.
     *
     * @param requestParams RequestParams required in the method
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public static StringGetParamRequest createRequest(RequestParams requestParams, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        final String parameterEncodedUrl = requestParams.getParameters() != null ? requestParams.getUrl() + "?" + Utility.buildQueryString(requestParams.getParameters()) : requestParams.getUrl();
        AppLog.d("CreateRequest URL:" + parameterEncodedUrl);
        RequestParams newRequestParams = new RequestParams(requestParams.getMethod(), parameterEncodedUrl, requestParams.getHeaders(), requestParams.getParameters(), requestParams.getTag());
        return new StringGetParamRequest(newRequestParams, listener, errorListener);
    }
}
