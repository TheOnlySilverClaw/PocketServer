package http;

import java.net.URI;
import java.util.HashMap;
import java.util.function.Function;

import http.protocol.HttpRequest;
import http.protocol.HttpResponse;

public class HttpRouter {

	private final HashMap<URI, Function<HttpRequest, HttpResponse>> routes = new HashMap<>();
	
	final HttpResponse respond(HttpRequest request) throws Exception {
		
		if(!routes.containsKey(request.getResource())) {
			throw new Exception("Unknown resource: " + request.getResource());
		}
		return routes.get(request.getResource()).apply(request);
	}
	
	public final void route(URI resource,
			Function<HttpRequest, HttpResponse> action) {
		routes.put(resource, action);
	}
}
