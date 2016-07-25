package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpConnection implements Callable<String>{

	protected static final String LINEBREAK = "\r\n";
	protected static final Charset CHARSET = Charset.forName("utf-8");
	
	private final Scanner scanner;
	
	public HttpConnection(Socket client) throws IOException {
		scanner = new Scanner(client.getInputStream());
	}
	
	private HttpRequest parse() throws Exception {
		
		scanner.useDelimiter(LINEBREAK);

		HttpMethod method;
		URI resource;
		
		if(scanner.hasNext()) {
			String requestLine [] = scanner.next().split(" ");
			method = HttpMethod.valueOf(requestLine[0]);
			resource = URI.create(requestLine[1]);
		} else {
			throw new Exception("Missing request line!");
		}
		
		InetAddress hostAddress;
		int hostPort;
		
		if(scanner.hasNext()) {
			String hostLine = scanner.next();
			if(hostLine.startsWith("Host: ")) {
				String [] hostParts = hostLine.substring(6).split(":");
				hostAddress = InetAddress.getByName(hostParts[0]);
				hostPort = Integer.parseInt(hostParts[1]);
			} else {
				throw new Exception("Invalid host line: " + hostLine);
			}
		} else {
			throw new Exception("Missing host line!");
		}
		
		Pattern headerPattern = Pattern.compile("([\\w-]+): (.*)");
		HashMap<String, String> headerMap = new HashMap<>();
		
		while(scanner.hasNext()) {
			String line = scanner.next();
			// body start
			if(line.isEmpty()) break;
			Matcher matcher = headerPattern.matcher(line);
			if(matcher.matches()) {
				headerMap.put(matcher.group(1), matcher.group(2));
			} else {
				throw new IllegalArgumentException(
						"Invalid header line: " + line);
			}
		}
		
		Object userAgent = headerMap.get("User-Agent");
		boolean keepAlive = headerMap.get("Connection").equals("keep-alive");
	return new HttpRequest(method, resource, 1, 1,
			hostAddress, hostPort, userAgent, keepAlive);
	}
	

	@Override
	public String call() throws Exception {
		
		HttpRequest request = parse();
		
		HttpResponse response = new HttpResponse();

		StringBuilder printer = new StringBuilder(1024);
		
		// only use HTTP 1.1 for now
		printer.append("HTTP/1.1");
		printer.append(" ");
		printer.append(response.getStatus().getCode());
		printer.append(" ");
		printer.append(response.getStatus().getReason());
		printer.append(LINEBREAK);
		
		byte [] body = response.getBody().getBytes(CHARSET);
		
		printer.append("Content-Length: ");
		printer.append(body.length);
		
		printer.append(LINEBREAK);
		
		response.cookies().forEachRemaining(cookie -> {
			printer.append("Set-Cookie: ");
			printer.append(cookie.toCookieString());
			printer.append(LINEBREAK);
		});
		
		printer.append(LINEBREAK);
		printer.append(body);
		
		return printer.toString();
	}
}
