package com.volleynet.network.params;


import com.volleynet.utils.ApiSignature;
import com.volleynet.utils.AppLog;
import com.volleynet.utils.Utility;

import java.util.HashMap;

/**
 * Sets the Auth token and signature by default, if you don't want to use default
 * headers then use RequestParam class instead
 */
public class SignedRequestParams extends RequestParams {

    /**
     * Creates the signed request param using the given token and secret and sets the headers using
     * signature key and token key
     *
     * @param method       http method e.g. POST/GET
     * @param url          API url
     * @param parameters   parameters
     * @param apiSecret    api secret
     * @param signatureKey signature key in headers
     * @param apiTokenKey  token key in headers
     * @param apiToken     api token
     * @param tag          tag for volley request can be null
     */
    public SignedRequestParams(int method, String url, HashMap<String, String> parameters, String apiSecret, String signatureKey, String apiTokenKey, String apiToken, Object tag) {
        this.url = url;
        this.method = method;
        this.parameters = parameters;
        this.tag = tag;
        // get the string type of http method for signature

        final String httpMethodStr = Utility.getHttpMethodString(method);
        // create the signature
        String signature = ApiSignature.createSignature(apiToken, apiSecret, parameters, url, httpMethodStr);
        AppLog.d("API trace: X-Api-Signature : " + signature);

        // create the headers and set it
        HashMap<String, String> headers = new HashMap<>();
        headers.put(apiTokenKey, apiToken);
        headers.put(signatureKey, signature);
        this.headers = headers;
    }
}
