package http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class HttpReader extends InputStreamReader {

	public HttpReader(InputStream in) {
		super(in, Charset.forName("US-ASCII"));
	}

	public String readLine() {
		
		return null;
	}
}
