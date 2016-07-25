package test.manual;

import java.net.InetAddress;
import java.util.concurrent.Executors;

import http.HttpResponse;
import server.PocketServer;

/**
 * This is for testing manually, until I figure out a good way to test
 * this whole HTTP stuff in unit tests...
 */
public class Start {

	public static void main(String[] args) throws Throwable {
		
		PocketServer server = new PocketServer(8080, 2,
				InetAddress.getByName("192.168.178.22"),
				Executors.newSingleThreadScheduledExecutor());
		server.start();
		
	}
	
	public static HttpResponse test(String name, int id) {
		return new HttpResponse();
	}
}
