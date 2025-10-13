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
    public void addPathParam(String name, String value) { this.pathParams.put(name, value); }



    // Get Authorization token
    public String getAuthToken() {
        String authHeader = getHeader("authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Get path segments for REST routing
    public String[] getPathSegments() {
        if (path == null || path.equals("/")) {
            return new String[0];
        }
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        return cleanPath.split("/");
    }
}