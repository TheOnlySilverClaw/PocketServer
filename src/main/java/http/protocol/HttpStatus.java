package http.protocol;

public enum HttpStatus {

	OK(200, "OK"),
	BAD_REQUEST(400, "Bad Request");
	
	private final int code;
	private final String reason;
	
	private HttpStatus(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	public final int getCode() {
		return code;
	}

	public final String getReason() {
		return reason;
	}
}
