package http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.Charset;

import server.Connection;

public class HttpSession implements Connection {

	protected static final String LINEBREAK = "\r\n";
	protected static final Charset CHARSET = Charset.forName("utf-8");
	
	private final HttpRequestParser parser;
	private final Socket client;
	
	public HttpSession(Socket client) throws IOException {
		this.client = client;
		this.parser = new HttpRequestParser();
	}

	

	@Override
	public void run() {
		
		HttpRequest request = null;
		try {
			request = parser.parse(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HttpResponse response = new HttpResponse();
		response.setBody("hello");
		
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrintWriter printer = new PrintWriter(writer);
		
		// only use HTTP 1.1 for now
		printer.append("HTTP/1.1");
		printer.append(" ");
		printer.print(response.getStatus().getCode());
		printer.append(" ");
		printer.append(response.getStatus().getReason());
		printer.append(LINEBREAK);
		
		byte [] body = response.getBody().getBytes(CHARSET);
		
		printer.append("Content-Length: ");
		printer.print(body.length);
		
		printer.append(LINEBREAK);
		
		response.cookies().forEachRemaining(cookie -> {
			printer.append("Set-Cookie: ");
			printer.append(cookie.toCookieString());
			printer.append(LINEBREAK);
		});
		
		printer.append(LINEBREAK);
		printer.print(response.getBody());
		printer.flush();
	}
}
