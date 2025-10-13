package at.technikum.server.http;

public class Response {
    private int status;
    private String body;
    private String contentType = "application/json";

    public Response(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() { return status; }
    public String getBody() { return body; }
    public String getContentType() { return contentType; }

    public void setContentType(String contentType) { this.contentType = contentType; }
}