package test.manual;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import http.HttpCookie;
import server.PocketServer;

/**
 * This is for testing manually, until I figure out a good way to test
 * this whole HTTP stuff in unit tests...
 */
public class Start {

	public static void main(String[] args) throws Throwable {

		
		PocketServer server = new PocketServer();
		CompletableFuture.runAsync(server::start);
		Thread.sleep(30000);
		server.stop();
		
	}
}
