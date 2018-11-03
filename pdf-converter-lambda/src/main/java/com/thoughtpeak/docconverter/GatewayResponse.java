package com.thoughtpeak.docconverter;

import java.util.HashMap;
import java.util.Map;

public class GatewayResponse {

    private boolean isBase64Encoded = false;

    private String statusCode = "200";

    private Map<String,String> headers = new HashMap<String, String>();

    private String body = "";

    public GatewayResponse(){}

    public GatewayResponse(String statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public boolean isBase64Encoded() {
        return isBase64Encoded;
    }

    public void setBase64Encoded(boolean base64Encoded) {
        isBase64Encoded = base64Encoded;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
