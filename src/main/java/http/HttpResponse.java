package http;

import java.util.ArrayDeque;
import java.util.Iterator;

public class HttpResponse {

	private HttpStatus status;
	//using my own implementation, since java.net.HttpCookie is just plain useless...
	private ArrayDeque<HttpCookie> cookies;
	private String body;


	public HttpResponse() {
		this.status = HttpStatus.OK;
		this.cookies = new ArrayDeque<>(8);
	}

	public final HttpStatus getStatus() {
		return status;
	}


	public final void setStatus(HttpStatus status) {
		this.status = status;
	}

	public HttpCookie findCookie(String name) {
		for(HttpCookie cookie : cookies) {
			if(cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}

	public void addCookie(HttpCookie cookie) {
		cookies.add(cookie);
	}
	
	public Iterator<HttpCookie> cookies() {
		return cookies.iterator();
	}

	public final String getBody() {
		return body;
	}


	public final void setBody(String body) {
		this.body = body;
	}
}
