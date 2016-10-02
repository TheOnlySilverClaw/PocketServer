package test.manual;

import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.Executors;

import http.HttpRouter;
import http.protocol.HttpResponse;
import http.protocol.HttpStatus;
import server.PocketServer;

/**
 * This is for testing manually, until I figure out a good way to test
 * this whole HTTP stuff in unit tests...
 */
public class Start {

	public static void main(String[] args) throws Throwable {
		
		HttpRouter router = new HttpRouter();
		router.route(URI.create("/"), request -> {
			HttpResponse response = new HttpResponse();
			
			response.setBody("<h1>Hello</h1>");
			response.setStatus(HttpStatus.OK);
			
			return response;
		});
		
		router.route(URI.create("/hello"), request -> {
			HttpResponse response = new HttpResponse();
			response.setBody("hello!");
			return response;
		});

		PocketServer server = new PocketServer(8080, 2,
				InetAddress.getByName("192.168.178.22"),
				Executors.newSingleThreadScheduledExecutor(),
				router);
		server.start();
	}
}
