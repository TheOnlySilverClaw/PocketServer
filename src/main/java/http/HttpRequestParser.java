package http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import http.protocol.HttpMethod;
import http.protocol.HttpRequest;

/**
 * Temporary solution. TODO: find alternative to Scanner
 *
 */
public class HttpRequestParser {

	public HttpRequest parse(InputStream in) throws Exception {

		// should be closed by session
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(in);
	
		scanner.useDelimiter("\r\n");

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
	return new HttpRequest(method, resource);
	}
}
