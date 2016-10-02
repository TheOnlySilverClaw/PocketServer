package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.HttpHandler;
import http.HttpRouter;

public class PocketServer {

	private final Logger log = LoggerFactory.getLogger(PocketServer.class);
	
	private final ExecutorService executorService;
	private final ServerSocket server;
	private boolean run;
	private final ConnectionHandler connectionHandler;
	
	public PocketServer() throws UnknownHostException, IOException {
		this(8080, 16, InetAddress.getLocalHost(),
				Executors.newCachedThreadPool(), new HttpRouter());
	}
	
	public PocketServer(int port, int backlog, InetAddress bindAddr,
			ExecutorService executorService, HttpRouter router)
			throws IOException {
		this.server = new ServerSocket(port, backlog, bindAddr);
		log.info("Created server on {}:{} with a backlog of {} connections.",
				bindAddr.getHostAddress(), port, backlog);
		this.executorService = executorService;
		this.run = true;
		this.connectionHandler = new HttpHandler(router);
	}

	public void start() {
	
		log.info("Starting {} at {}", getClass().getSimpleName(), LocalDateTime.now());
			
		while(run) {
			try {
				log.debug("Waiting for connection...");
				Socket client = server.accept();
				log.debug("Accepted connection: {}", client);
				executorService.submit(connectionHandler.handle(client)::run);
				log.debug("Delegated to handler.");
			} catch (SocketException e) {
				log.debug("Socket closed successfully.");
			} catch (IOException e) {
				log.debug("test");
				log.error("Exception while waiting for connection.", e);
			}
		}
	}
	
	public void stop() {
		
		log.info("Stopping {} at {}", getClass().getSimpleName(), LocalDateTime.now());
		run = false;
		try {
			server.close();
		} catch (IOException e) {
			log.error("Exception while closing server socket.", e);
		}
		log.info("Stopped.");
	}
}
