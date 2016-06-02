package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class PocketServer {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public void start() {
		log.info("Starting {} at {}", getClass().getSimpleName(), Instant.now());
	
	}
	
	public void stop() {
		log.info("Stopping {} at {}", getClass().getSimpleName(), Instant.now());

	}
}
