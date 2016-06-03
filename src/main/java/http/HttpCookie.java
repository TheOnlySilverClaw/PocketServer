package http;

public class HttpCookie {

	private final String name;
	private final String value;
	
	private String path;
	private String domain;
	private long expires;
	
	public HttpCookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

}
