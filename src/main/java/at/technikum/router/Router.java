package at.technikum.router;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    private final Map<String, Function<Request, Response>> routes = new HashMap<>();

    public void addRoute(String method, String path, Function<Request, Response> handler) {
        String key = method + ":" + path;
        routes.put(key, handler);
    }

    public Function<Request, Response> findEndpoint(Request request) {
        String method = request.getMethod();
        String path = request.getPath();

        // Exact match
        String key = method + ":" + path;
        if (routes.containsKey(key)) return routes.get(key);

        //  Match with path parameters
        for (String routeKey : routes.keySet()) {
            String[] parts = routeKey.split(":");
            if (!parts[0].equals(method)) continue; // method must match

            String routePath = parts[1];
            Map<String, String> params = matchPathWithParams(routePath, path);
            if (params != null) {
                request.setPathParams(params);
                return routes.get(routeKey);
            }
        }

        return null; // no match
    }

    private Map<String, String> matchPathWithParams(String route, String path) {
        String[] routeParts = route.split("/");
        String[] pathParts = path.split("/");

        if (routeParts.length != pathParts.length) return null;

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < routeParts.length; i++) {
            if (routeParts[i].startsWith("{") && routeParts[i].endsWith("}")) {
                String key = routeParts[i].substring(1, routeParts[i].length() - 1);
                params.put(key, pathParts[i]);
            } else if (!routeParts[i].equals(pathParts[i])) {
                return null;
            }
        }
        return params;
    }
}
