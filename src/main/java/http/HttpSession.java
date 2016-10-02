package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.protocol.HttpCookie;
import http.protocol.HttpMethod;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import server.Connection;

public class HttpSession implements Connection {

	protected static final String LINEBREAK = "\r\n";
	protected static final Charset CHARSET = Charset.forName("utf-8");

	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final Socket client;
	private final Function<HttpRequest, HttpResponse> responseProvider;
	
	public HttpSession(Socket client,
			Function<HttpRequest, HttpResponse> responseProvider) {
		
		this.client = client;
		this.responseProvider = responseProvider;
	}

	

	@Override
	public void run() {
		
		BufferedReader stream = null;
		try {
			stream = new BufferedReader(
					new InputStreamReader(client.getInputStream(), "us-ascii"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpRequest request = null;
		try {
			request = parse(stream);
		} catch (Exception e1) {
			log.error("Failed to parse request! ({} : {})",
					e1.getClass(), e1.getMessage());
		}
		
		HttpResponse response = responseProvider.apply(request);
		
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
		
		for(Iterator<HttpCookie> cookies = response.cookies();
				cookies.hasNext(); ) {
			printer.append("Set-Cookie: ");
			printer.append(cookies.next().toCookieString());
			printer.append(LINEBREAK);
		}
		
		printer.append(LINEBREAK);
		printer.print(response.getBody());
		printer.flush();
	}



	private HttpRequest parse(BufferedReader reader) throws Exception {
		
		HttpMethod method;
		URI resource;
	
		HttpRequest request;
		
		String [] requestLine = reader.readLine().split(" ");
		if(requestLine.length != 3) {
			throw new IllegalArgumentException(
					"Wrong number of request line parts: "
							+ requestLine.length);
		}

		if(!requestLine[2].equals("HTTP/1.1")) {
			throw new UnsupportedOperationException(
					"Does not support: " + requestLine[2]);
		}
		
		request = new HttpRequest(
				HttpMethod.valueOf(requestLine[0]),
				new URI(requestLine[1])
				);


		String hostLine = reader.readLine();
		if(hostLine != null && hostLine.startsWith("Host:")) {
			String [] host = hostLine.substring("Host: ".length()).split(":");
			request.setHost(new InetSocketAddress(
					host[0], Integer.parseInt(host[1])));
		}
		
		System.out.println(reader.readLine());
		return request;
	}
}
