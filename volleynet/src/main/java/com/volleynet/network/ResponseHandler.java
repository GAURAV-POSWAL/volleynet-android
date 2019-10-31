package com.volleynet.network;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.volleynet.utils.AppLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @author raghav
 * Created on 6/5/16.
 * <p>
 * Class to handle api call responses and call respective method for success and failure
 */
public class ResponseHandler {

    // User message used in the response object if malformed json response received.
    private final static String userMessageServerFault = "We’ve got some problem in our servers. Please try again in a while.";
    // User message used in the response object if not found in the response json or response is null.
    private final static String userMessageConnectivityProblem = "We’re having some problem communicating to our servers. " +
            "Please check your internet connection or try again later.";
    // used to display failure status string
    private final static String failureStatus = "failure";
    // used to display success status string
    private final static String successStatus = "success";
    // used to show http status while handling success response
    private final static int httpCodeSuccess = 200;

    /**
     * Handles the success response string and only calls success if responseString is not null and
     * the response status string is success in json response for AM apis
     * and always for third party api.
     *
     * @param responseString  Response string
     * @param requestComplete RequestComplete call back
     * @param apiType         type of api that was hit, to decide which way to process the response
     */
    public static void handleSuccessResponse(String responseString, RequestComplete requestComplete, ApiType apiType) {
        if (responseString == null) {
            final int statusCodeNullResponse = 90607;
            final String debugMessage = "Response was null.";
            ResponseObject responseObject = new ResponseObject(httpCodeSuccess, statusCodeNullResponse, userMessageConnectivityProblem, failureStatus, debugMessage, null);
            requestComplete.requestFailed(responseObject);
        } else {

            // Here ApiType is an enum which is used to differentiate
            // whether the api response is from an Aspiring Minds internal api
            // or a third party api to select which way to parse and process it.

            // so, if apiType is AM_API we call handleSuccessForAMApiResponse to parse it for status string, api code, data etc
            // from the received api json response
            if (ApiType.AM_API == apiType) {
                handleSuccessForAMApiResponse(responseString, requestComplete);
            }

            // otherwise(for THIRD_PARTY_API) we take the response string as the final response data and send it as it is in requestSuccess.
            else {
                final String debugMessage = "Response success.";
                requestComplete.requestSuccess(new ResponseObject(httpCodeSuccess, httpCodeSuccess, "API call successful.", successStatus, debugMessage, responseString));
            }

        }
    }

    /**
     * Handles the success response string and only calls success if it has response status - success
     * specifically for Aspiring minds api format response
     *
     * @param responseString  Response string
     * @param requestComplete RequestComplete call back
     */
    private static void handleSuccessForAMApiResponse(String responseString, RequestComplete requestComplete) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            final String codeKey = "code";
            final String messageKey = "message";
            final String statusKey = "status";
            final String dataKey = "data";
            if (jsonObject.has(statusKey)) {
                if (successStatus.equalsIgnoreCase(jsonObject.optString(statusKey).trim())) {
                    // success
                    final int statusCodeWasMissing = 90606;
                    final String message = jsonObject.optString(messageKey, successStatus);
                    final Object data = jsonObject.opt(dataKey);
                    final int apiStatusCode = jsonObject.optInt(codeKey, statusCodeWasMissing);
                    ResponseObject responseObject = new ResponseObject(httpCodeSuccess, apiStatusCode, message, successStatus, "API call success.", data);
                    requestComplete.requestSuccess(responseObject);
                } else {
                    // failure
                    final int statusWasNotSuccess = 90608;
                    final String debugMessage = "Status was not success.";
                    final int apiStatusCode = jsonObject.optInt(codeKey, statusWasNotSuccess);
                    final String message = jsonObject.optString(messageKey, userMessageServerFault);
                    final Object data = jsonObject.opt(dataKey);
                    ResponseObject responseObject = new ResponseObject(httpCodeSuccess, apiStatusCode, message, failureStatus, debugMessage, data);
                    requestComplete.requestFailed(responseObject);
                }
            } else {
                // failure
                final int responseStatusNotPresent = 90609;
                final String debugMessage = "Response status was not present.";
                final String message = jsonObject.optString(messageKey, userMessageServerFault);
                final Object data = jsonObject.opt(dataKey);
                final int apiStatusCode = jsonObject.optInt(codeKey, responseStatusNotPresent);
                ResponseObject responseObject = new ResponseObject(httpCodeSuccess, apiStatusCode, message, failureStatus, debugMessage, data);
                requestComplete.requestFailed(responseObject);
            }
        } catch (JSONException e) {
            // failure
            final int statusCodeMalformedJSON = 90610;
            final String debugMessage = "There is a syntax error in the JSON string.";
            ResponseObject responseObject = new ResponseObject(httpCodeSuccess, statusCodeMalformedJSON, userMessageServerFault, failureStatus, debugMessage, null);
            requestComplete.requestFailed(responseObject);
        }
    }

    /**
     * Handles the error response, and calls the requestComplete.requestFailed(responseObject) for various cases
     *
     * @param error           Volley error
     * @param requestComplete RequestComplete call back
     * @param apiType         type of api that was hit, to decide which way to process the response
     */
    public static void handleFailureResponse(VolleyError error, RequestComplete requestComplete, ApiType apiType) {
        if (error != null) {
            if (error.networkResponse != null) {
                if (error.networkResponse.data != null) {
                    String responseString;
                    try {
                        responseString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    } catch (UnsupportedEncodingException e) {
                        responseString = new String(error.networkResponse.data);
                    }

                    AppLog.d("Network response:" + responseString);

                    // Here ApiType is an enum which is used to differentiate
                    // whether the api response is from an Aspiring Minds internal api
                    // or a third party api to select which way to parse and process it.

                    // so, if apiType is AM_API we call handleFailureForAMApiResponse to parse it for status string, api code, data etc
                    // from the received api json response
                    if (ApiType.AM_API == apiType) {
                        handleFailureForAMApiResponse(responseString, error, requestComplete);
                    }

                    // otherwise(for THIRD_PARTY_API) we send null into the response data and httpStatusCode received in requestFailed
                    else {
                        requestComplete.requestFailed(new ResponseObject(error.networkResponse.statusCode, error.networkResponse.statusCode, userMessageServerFault, failureStatus, error.getMessage(), null));
                    }

                } else {
                    AppLog.d("The response data was null.");
                    final int statusCodeResponseDataNull = 90612;
                    final String debugMessage = "The response data was null.";
                    ResponseObject responseObject = new ResponseObject(error.networkResponse.statusCode, statusCodeResponseDataNull, userMessageServerFault, failureStatus, debugMessage, null);
                    requestComplete.requestFailed(responseObject);
                }
            } else {
                AppLog.d("The network response was null.");
                final int statusCodeNetworkResponseNull = 90613;
                final String debugMessage = "The network response was null.";
                ResponseObject responseObject = new ResponseObject(statusCodeNetworkResponseNull, statusCodeNetworkResponseNull, userMessageConnectivityProblem, failureStatus, debugMessage, null);
                requestComplete.requestFailed(responseObject);
            }
        } else {
            AppLog.d("Volley error was null.");
            final int statusCodeVolleyErrorNull = 90614;
            final String debugMessage = "Volley error was null.";
            ResponseObject responseObject = new ResponseObject(statusCodeVolleyErrorNull, statusCodeVolleyErrorNull, userMessageServerFault, failureStatus, debugMessage, null);
            requestComplete.requestFailed(responseObject);
        }
    }

    /**
     * Handles the error response, and calls the requestComplete.requestFailed(responseObject) for various cases
     * specifically for Aspiring minds api format response
     *
     * @param error           Volley error
     * @param requestComplete RequestComplete call back
     */
    private static void handleFailureForAMApiResponse(String responseString, VolleyError error, RequestComplete requestComplete) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            final String codeKey = "code";
            final String messageKey = "message";
            final String statusKey = "status";
            final String dataKey = "data";
            if (jsonObject.has(statusKey)) {
                final String message = jsonObject.optString(messageKey, userMessageServerFault);
                final String status = jsonObject.optString(statusKey, failureStatus);
                final String debugMessage = error.getMessage();
                final Object data = jsonObject.opt(dataKey);
                final int statusCodeWasMissing = 90608;
                final int apiStatusCode = jsonObject.optInt(codeKey, statusCodeWasMissing);
                ResponseObject responseObject = new ResponseObject(error.networkResponse.statusCode, apiStatusCode, message, status, debugMessage, data);
                requestComplete.requestFailed(responseObject);
            } else {
                final int responseStatusNotPresent = 90609;
                final String debugMessage = "Response status was not present.";
                final String message = jsonObject.optString(messageKey, userMessageServerFault);
                final Object data = jsonObject.opt(dataKey);
                ResponseObject responseObject = new ResponseObject(error.networkResponse.statusCode, responseStatusNotPresent, message, failureStatus, debugMessage, data);
                requestComplete.requestFailed(responseObject);
            }
        } catch (JSONException e) {
            final int statusCodeMalformedJSON = 90610;
            final String debugMessage = "There is a syntax error in the JSON string ";
            ResponseObject responseObject = new ResponseObject(error.networkResponse.statusCode, statusCodeMalformedJSON, userMessageServerFault, failureStatus, debugMessage, null);
            requestComplete.requestFailed(responseObject);
        }
    }

    /**
     * Response error codes
     * 90606 : response code was missing
     * 90607 : Response was null.
     * 90608 : Response status was not success.
     * 90609 : Response status was not present.
     * 90610 : There is a syntax error in the JSON string.
     * 90611 : Not used anywhere, initially it was for UnsupportedEncodingException
     * 90612 : The response data was null.
     * 90613 : The network response was null. So, no Http code available
     * 90614 : Volley error was null. So, no Http code available
     */

    public enum ApiType {
        AM_API(1),
        THIRD_PARTY_API(2);

        int code;

        ApiType(int code) {
            this.code = code;
        }
    }

}
