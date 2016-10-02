package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import http.protocol.HttpResponse;
import http.protocol.HttpStatus;
import server.ConnectionHandler;

public class HttpHandler implements ConnectionHandler {
	
	private final HashMap<InetAddress, HttpSession> sessions;
	private final HttpRouter router;
	
	public HttpHandler(HttpRouter router) {
		this.sessions = new HashMap<>();
		this.router = router;
	}
	
	public HttpSession handle(Socket client) throws IOException {
		HttpSession session = sessions.get(client.getInetAddress());
		if(session == null) {
			session = new HttpSession(client, t -> {
				try {
					return router.respond(t);
				} catch (Exception e) {
					e.printStackTrace();
					HttpResponse fail = new HttpResponse();
					fail.setStatus(HttpStatus.BAD_REQUEST);
					return fail;
				}
			});
			sessions.put(client.getInetAddress(), session);
		}
		return session;
	}
}
