package http;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class HttpCookie {

	private static final DateTimeFormatter rfc1223Formatter = 
			DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);
	
	private final String name;
	private final String value;
	
	private String path;
	private String domain;
	private Instant expires;
	private Instant maxAge;
	private boolean httpOnly;
	private boolean secure;
	
	public HttpCookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	
	public final String getPath() {
		return path;
	}


	public final void setPath(String path) {
		this.path = path;
	}


	public final String getDomain() {
		return domain;
	}


	public final void setDomain(String domain) {
		this.domain = domain;
	}


	public final Instant getExpires() {
		return expires;
	}


	public final void setExpires(Instant expires) {
		this.expires = expires;
	}


	public Instant getMaxAge() {
		return maxAge;
	}


	public void setMaxAge(Instant maxAge) {
		this.maxAge = maxAge;
	}


	public final boolean isHttpOnly() {
		return httpOnly;
	}


	public final void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}


	public final boolean isSecure() {
		return secure;
	}


	public final void setSecure(boolean secure) {
		this.secure = secure;
	}


	public final String getName() {
		return name;
	}


	public final String getValue() {
		return value;
	}


	/**
	 * @return a set-cookie-string, formatted in RFC 6265 standard
	 */
	public String toCookieString() {
		
		StringBuffer buffer= new StringBuffer(128);
		buffer.append(name);
		if(value != null) {
			buffer.append('=');
			buffer.append('"');
			buffer.append(value);
			buffer.append('"');
		}
		if(path != null) {
			buffer.append("; Path=");
			buffer.append(path);
		}
		if(domain != null) {
			buffer.append("; Domain=");
			buffer.append(domain);
		}
		if(expires != null) {
			buffer.append("; Expires=");
			buffer.append(rfc1223Formatter.format(expires));
		}
		if(maxAge != null) {
			buffer.append("; Max-Age=");
			buffer.append(rfc1223Formatter.format(maxAge));
		}
		if(httpOnly) {
			buffer.append("; HttpOnly");
		}
		if(secure) {
			buffer.append("; Secure");
		}
		return buffer.toString();
	}
}
