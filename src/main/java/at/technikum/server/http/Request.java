package at.technikum.server.http;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private String body;
    private Map<String, String> headers;
    private Map<String, String> pathParams;

    public Request() {
        this.headers = new HashMap<>();
        this.pathParams = new HashMap<>();
    }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public String getHeader(String name) { return headers.get(name); }
    public void addHeader(String name, String value) { this.headers.put(name, value); }

    public Map<String, String> getPathParams() { return pathParams; }
    public void setPathParams(Map<String, String> pathParams) { this.pathParams = pathParams; }

    public String getPathParam(String name) { return pathParams.get(name); }




}