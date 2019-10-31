package com.volleynet.network;

public class ResponseObject {
    // Http status code received in response while hitting an api
    private int httpStatusCode;
    // Status code in the response (in json "{"code":309, etc. etc.}"
    private int apiStatusCode;
    // Message to be displayed to the user, shouldn't be used since we'll map error codes to the user messages
    private String message;
    // Status success/failure
    private String apiStatus;
    // Debug message for developer
    private String debugMessage;
    // JSONObject/JSONArray/String etc. what ever your api success "data" key returns
    private Object data;

    public ResponseObject(int httpStatusCode, int apiStatusCode, String message, String apiStatus, String debugMessage, Object data) {
        this.httpStatusCode = httpStatusCode;
        this.apiStatusCode = apiStatusCode;
        this.message = message;
        this.apiStatus = apiStatus;
        this.debugMessage = debugMessage;
        this.data = data;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public int getApiStatusCode() {
        return apiStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getApiStatus() {
        return apiStatus;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public Object getData() {
        return data;
    }

    /**
     * I'll deprecate it, created just to make api backward compatible.
     * I'll remove this once the parsing is done using `data`
     */
    public String getJsonResponse() {
        String json = "{\"code\":" + apiStatusCode +
                "," +
                "\"status\":" + "\"" + apiStatus + "\"" +
                "," +
                "\"data\":" + data.toString() +
                "," +
                "\"message\":" + "\"" + message + "\"" +
                "}";
        return json;
    }

    @Override
    public String toString() {
        return "Http status code:" + httpStatusCode + ", API status code:" + apiStatusCode + ", Message:" + message + ", Debug message:" + debugMessage;
    }
}
