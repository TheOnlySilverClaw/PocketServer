package http.protocol;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * only supports HTTP/1.1
 * 
 * @author joel
 *
 */
public class HttpRequest {

	private final HttpMethod method;
	private final URI resource;
	
	private InetSocketAddress host;
	private int port;
	// not sure what, yet
	private Object userAgent;
	private boolean keepAlive;
	
	public HttpRequest(HttpMethod method, URI resource) {
		this.method = method;
		this.resource = resource;
	}

	public final HttpMethod getMethod() {
		return method;
	}

	public final URI getResource() {
		return resource;
	}

	public int getMajor() {
		return 1;
	}

	public int getMinor() {
		return 1;
	}

	public final InetSocketAddress getHost() {
		return host;
	}

	public final void setHost(InetSocketAddress inetSocketAddress) {
		this.host = inetSocketAddress;
	}

	public final int getPort() {
		return port;
	}

	final void setPort(int port) {
		this.port = port;
	}

	public final Object getUserAgent() {
		return userAgent;
	}

	final void setUserAgent(Object userAgent) {
		this.userAgent = userAgent;
	}

	public final boolean isKeepAlive() {
		return keepAlive;
	}

	final void keepAlive() {
		this.keepAlive = true;
	}
}
