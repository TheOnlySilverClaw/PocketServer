package http;

import java.net.InetAddress;
import java.net.URI;

public class HttpRequest {

	private final HttpMethod method;
	private final URI resource;
	private final int major;
	private final int minor;
	private final InetAddress host;
	private final int port;
	// not sure what, yet
	private final Object userAgent;
	private final boolean keepAlive;
	
	public HttpRequest(
			HttpMethod method, URI resource, int major, int minor,
			InetAddress host, int port,
			Object userAgent, boolean keepAlive) {
		this.method = method;
		this.resource = resource;
		this.major = major;
		this.minor = minor;
		this.host = host;
		this.port = port;
		this.userAgent = userAgent;
		this.keepAlive = keepAlive;
	}

	public final HttpMethod getMethod() {
		return method;
	}

	public final URI getResource() {
		return resource;
	}

	public final int getMajor() {
		return major;
	}

	public final int getMinor() {
		return minor;
	}

	public final InetAddress getHost() {
		return host;
	}

	public final int getPort() {
		return port;
	}

	public final Object getUserAgent() {
		return userAgent;
	}

	public final boolean isKeepAlive() {
		return keepAlive;
	}
}
