package http;

import java.util.List;

public class HttpResponse {

	private HttpStatus status;
	//using my own implementation, since java.net.HttpCookie is just plain useless...
	private List<HttpCookie> cookies;
	private String body;
	private int contentLength;


	public HttpResponse() {
		this.status = HttpStatus.OK;
	}

	public final HttpStatus getStatus() {
		return status;
	}


	public final void setStatus(HttpStatus status) {
		this.status = status;
	}


	public final List<HttpCookie> getCookies() {
		return cookies;
	}


	public final void setCookies(List<HttpCookie> cookies) {
		this.cookies = cookies;
	}


	public final String getBody() {
		return body;
	}


	public final void setBody(String body) {
		this.body = body;
		this.contentLength = body.getBytes().length;
	}
	
	
	public int getContentLength() {
		return contentLength;
	}
}
