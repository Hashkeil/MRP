package at.technikum.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.service.AuthService;
import at.technikum.model.User;
import org.json.JSONObject;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


     // POST /api/users/register
    public Response register(Request request) {
        try {
            JSONObject json = new JSONObject(request.getBody());
            String username = json.getString("username");
            String password = json.getString("password");

            User user = authService.register(username, password);

            JSONObject response = new JSONObject();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("password", user.getPassword());
            response.put("message", "User registered successfully");

            return new Response(Status.CREATED.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.BAD_REQUEST.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


     // POST /api/users/login
    public Response login(Request request) {
        try {
            JSONObject json = new JSONObject(request.getBody());
            String username = json.getString("username");
            String password = json.getString("password");

            User user = authService.login(username, password);

            JSONObject response = new JSONObject();
            response.put("token", user.getToken());
            response.put("username", user.getUsername());
            response.put("message", "Login successful");

            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


      //POST /api/users/logout
    public Response logout(Request request) {
        try {
            String authHeader = request.getHeader("authorization");
            String token = authService.extractToken(authHeader);

            authService.logout(token);

            JSONObject response = new JSONObject();
            response.put("message", "Logout successful");

            return new Response(Status.OK.getCode(), response.toString());
        } catch (Exception e) {
            return new Response(Status.UNAUTHORIZED.getCode(),
                    new JSONObject().put("error", e.getMessage()).toString());
        }
    }


}