package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.time.Instant;
import java.io.IOException;
import java.net.*;

public class PocketServer {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final ServerSocket server;
	private boolean run;
	
	public PocketServer() throws UnknownHostException, IOException {
		this(8080, 16, InetAddress.getLocalHost());
	}
	
	public PocketServer(int port, int backlog, InetAddress bindAddr)
			throws IOException {
		server = new ServerSocket(port, backlog, bindAddr);
		log.info("Created server on {}:{} with a backlog of {} connections.",
				bindAddr.getHostAddress(), port, backlog);
		run = true;
	}
	
	public void start() {
		log.info("Starting {} at {}", getClass().getSimpleName(), Instant.now());
	
		while(run) {
			try {
				log.debug("Waiting for connection...");
				Socket client = server.accept();
				log.debug("Accepted connection: {}", client);
			} catch (IOException e) {
				log.error("Exception while waiting for connection.", e);
			}
		}
	}
	
	public void stop() {
		log.info("Stopping {} at {}", getClass().getSimpleName(), Instant.now());

	}
}
