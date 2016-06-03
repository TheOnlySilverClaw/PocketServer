package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PocketServer {

	private final Logger log = LoggerFactory.getLogger(PocketServer.class);
	
	//private final ExecutorService executorService;
	private final ServerSocket server;
	private boolean run;
	
	public PocketServer() throws UnknownHostException, IOException {
		this(8080, 16, InetAddress.getLocalHost(),
				Executors.newCachedThreadPool());
	}
	
	public PocketServer(int port, int backlog, InetAddress bindAddr,
			ExecutorService executorService)
			throws IOException {
		this.server = new ServerSocket(port, backlog, bindAddr);
		log.info("Created server on {}:{} with a backlog of {} connections.",
				bindAddr.getHostAddress(), port, backlog);
		//this.executorService = executorService;
		run = true;
	}
	
	public void start() {
	
		log.info("Starting {} at {}", getClass().getSimpleName(), LocalDateTime.now());
	
		while(run) {
			try {
				log.debug("Waiting for connection...");
				Socket client = server.accept();
				log.debug("Accepted connection: {}", client);
				// TODO: Close or keep connections alive correctly.
				ClientHandler handler = new ClientHandler(client);
				CompletableFuture.runAsync(handler::handle).thenRun(handler::close);
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
