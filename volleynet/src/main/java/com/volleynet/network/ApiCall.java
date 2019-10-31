package com.volleynet.network;

import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.volleynet.application.BaseApplication;
import com.volleynet.network.params.RequestParams;
import com.volleynet.network.request.StringGetParamRequest;
import com.volleynet.network.request.StringRequest;
import com.volleynet.utils.AppLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * Class for web api calls
 */
public class ApiCall {
    public static final String EXTRA_FORCE_UPDATE_INFO = "EXTRA_FORCE_UPDATE_INFO";
    public static final int POST_REQUEST = Request.Method.POST;
    public static final int GET_REQUEST = Request.Method.GET;
    private static final String API_KEY_FORCE_UPDATE = "forceUpdate";
    private static final String API_KEY_DATA = "data";

    /**
     * This method used to make connection and fetch data from a 3rd party network Api such as wordpress with help of {@link Volley}
     * with StringRequest.
     *
     * @param requestParam    request parameter
     * @param requestComplete completion block
     * @return the request object
     */
    public static StringGetParamRequest stringRequestThirdPartyApi(final RequestParams requestParam, final RequestComplete requestComplete) {
        final StringGetParamRequest stringGetParamRequest = StringGetParamRequest.createRequest(requestParam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.d("API trace: NETWORK RESPONSE FOR : \n" + requestParam.getUrl() + "\n" + response);
                ResponseHandler.handleSuccessResponse(response, requestComplete, ResponseHandler.ApiType.THIRD_PARTY_API);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error);
                ResponseHandler.handleFailureResponse(error, requestComplete, ResponseHandler.ApiType.THIRD_PARTY_API);
            }
        });

        //set Retry policy
        stringGetParamRequest.setRetryPolicy(ApiCall.getRetryPolicy());
        //set params
        stringGetParamRequest.setRequestParams(requestParam);
        // disable caching by volley
        stringGetParamRequest.setShouldCache(false);

        //execute the request
        stringGetParamRequest.executeRequest();
        return stringGetParamRequest;
    }

    /**
     * This method used to make connection and fetch data from network with help of {@link Volley}
     * with StringRequest.
     *
     * @param requestParam    request parameter
     * @param requestComplete completion block
     * @return the request object
     */
    public static StringRequest stringRequest(final RequestParams requestParam, final RequestComplete requestComplete) {
        final StringRequest stringRequest = new StringRequest(requestParam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.d("API trace: NETWORK RESPONSE FOR : \n" + requestParam.getUrl() + "\n" + response);
                ResponseHandler.handleSuccessResponse(response, requestComplete, ResponseHandler.ApiType.AM_API);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error);
// Needs to remove the handle failure response

                String forceUpdateData = checkForceUpdate(error);
                if (null != forceUpdateData) {
                    Intent forceIntent;
                    forceIntent = new Intent();
                    forceIntent.putExtra(EXTRA_FORCE_UPDATE_INFO, forceUpdateData);
                    if (!BaseApplication.getApplication().handleForceUpdate(forceIntent)) {// case if handleForceUpdate returns false or this method is not overridden in the child app
        /*    final CLNetworkException exception = new CLNetworkException(error);
            AppLog.e(error);
            requestComplete.requestFailed(exception.getStatusCode(), exception);*/
                        ResponseHandler.handleFailureResponse(error, requestComplete, ResponseHandler.ApiType.AM_API);
                    }
                } else {
          /*final CLNetworkException exception = new CLNetworkException(error);
          AppLog.e(error);
          requestComplete.requestFailed(exception.getStatusCode(), exception);*/
                    ResponseHandler.handleFailureResponse(error, requestComplete, ResponseHandler.ApiType.AM_API);
                }
            }
        });

        //set Retry policy
        stringRequest.setRetryPolicy(ApiCall.getRetryPolicy());
        //set params
        stringRequest.setRequestParams(requestParam);
        // disable caching by volley
        stringRequest.setShouldCache(false);

        //execute the request
        stringRequest.executeRequest();
        return stringRequest;
    }

    /**
     * This is the Overloaded method of StringRequest.
     * <p/>
     * This method used to make connection and fetch data from network with help of {@link Volley}
     * with StringRequest.
     * <p/>
     * By using this Overloaded method can set the Timeout time in millisecond to increase the request time
     *
     * @param requestParam    request parameter
     * @param retryPolicy     retry policy
     * @param requestComplete completion block
     * @return the request object
     */
    public static StringRequest stringRequest(RequestParams requestParam, RetryPolicy retryPolicy, final RequestComplete requestComplete) {

        final StringRequest stringRequest = stringRequest(requestParam, requestComplete);

        if (retryPolicy != null) {
            stringRequest.setRetryPolicy(retryPolicy);
        }
// already getting executed once in stringRequest(RequestParams, RequestComplete)
//    //execute the request
//    stringRequest.executeRequest();
        return stringRequest;
    }

    /**
     * Use this method when you have parameters in GET request
     *
     * @param requestParam    Map of key/value required in get request
     * @param requestComplete completion block
     */
    public static StringGetParamRequest stringGetRequestWithParam(final RequestParams requestParam, final RequestComplete requestComplete) {
        final StringGetParamRequest stringGetParamRequest = StringGetParamRequest.createRequest(requestParam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.d("API trace: NETWORK RESPONSE FOR : \n" + requestParam.getUrl() + "\n" + response);
                ResponseHandler.handleSuccessResponse(response, requestComplete, ResponseHandler.ApiType.AM_API);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

// needs to check if handle failure response is needed since we don't want call back for force update
                String forceUpdateData = checkForceUpdate(error);
                if (null != forceUpdateData) {
                    Intent forceIntent = new Intent();
                    forceIntent.putExtra(EXTRA_FORCE_UPDATE_INFO, forceUpdateData);
                    if (!BaseApplication.getApplication().handleForceUpdate(forceIntent)) {// case if handleForceUpdate returns false or this method is not overridden in the child app
            /*final CLNetworkException exception = new CLNetworkException(error);
            AppLog.e(error);
            requestComplete.requestFailed(exception.getStatusCode(), exception);*/
                        ResponseHandler.handleFailureResponse(error, requestComplete, ResponseHandler.ApiType.AM_API);
                    }
                } else {
          /*final CLNetworkException exception = new CLNetworkException(error);
          AppLog.e(error);
          requestComplete.requestFailed(exception.getStatusCode(), exception);*/
                    ResponseHandler.handleFailureResponse(error, requestComplete, ResponseHandler.ApiType.AM_API);
                }
            }
        });


//TODO : need to check the execute request

        //set Retry policy
        stringGetParamRequest.setRetryPolicy(ApiCall.getRetryPolicy());
        //set params
        stringGetParamRequest.setRequestParams(requestParam);
        // disable caching by volley
        stringGetParamRequest.setShouldCache(false);
        //execute the request
        stringGetParamRequest.executeRequest();
        return stringGetParamRequest;
    }

    /**
     * Checks for forceUpdate param in the response from the server, if the param is present and has value 1 than this method returns true else false
     *
     * @param error {@link VolleyError}
     * @return true if forceUpdate has value 1,false otherwise whatever the reason may be
     */
    private static String checkForceUpdate(VolleyError error) {
        if (null != error) {
            NetworkResponse networkResponse = error.networkResponse;
            if (null != networkResponse) {
                byte[] data = networkResponse.data;
                if (null != data) {
                    try {
                        String response = new String(data, HttpHeaderParser.parseCharset(networkResponse.headers));
                        AppLog.d("Force" + response);
                        if (!response.isEmpty()) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(API_KEY_FORCE_UPDATE)) {
                                if (jsonObject.optInt(API_KEY_FORCE_UPDATE, 0) > 0) {
                                    return jsonObject.optString(API_KEY_DATA, null);
                                }

                            }
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        AppLog.e(e);
                    }
                }
            }
        }
        return null;
    }

    private static DefaultRetryPolicy getRetryPolicy() {
        final int TIMEOUT_SEC = 40;
        final int DEFAULT_MAX_RETRIES = 0;
        final int DEFAULT_TIMEOUT_MS = (int) TimeUnit.SECONDS.toMillis(TIMEOUT_SEC);

        return (new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}