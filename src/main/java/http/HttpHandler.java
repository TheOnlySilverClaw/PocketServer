package http;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import server.ConnectionHandler;

public class HttpHandler implements ConnectionHandler {
	
	private final HashMap<InetAddress, HttpSession> sessions;
	
	public HttpHandler() {
		this.sessions = new HashMap<>();
	}
	
	public HttpSession handle(Socket client) throws IOException {
		HttpSession session = sessions.get(client.getInetAddress());
		if(session == null) {
			session = new HttpSession(client);
			sessions.put(client.getInetAddress(), session);
		}
		return session;
	}
}
