package server;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.HttpMethod;
import http.HttpRequest;

import java.lang.AutoCloseable;

public class ClientHandler implements AutoCloseable {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private final Socket client;
	private final Scanner scanner;
	private final PrintStream printer;
	
	public ClientHandler(Socket client) throws IOException {
		this.client = client;
		this.scanner = new Scanner(client.getInputStream());
		this.printer = new PrintStream(client.getOutputStream());
	}

	public void handle() {

		try {
			HttpRequest request = parse();
			log.debug("received {} request for resource '{}'",
					request.getMethod(), request.getResource());
			// TODO: send proper response
			printer.println("HTTP/1.1 200 OK");
		} catch (Exception e) {
			log.debug("Exception while handling client.", e);
		}
	}
	
	private HttpRequest parse() throws Exception {
		
		scanner.useDelimiter("\r\n");

		HttpMethod method;
		URI resource;
		
		if(scanner.hasNext()) {
			String requestLine [] = scanner.next().split(" ");
			log.debug("request line: {}", Arrays.toString(requestLine));
			method = HttpMethod.valueOf(requestLine[0]);
			log.debug("method: {}", method);
			resource = URI.create(requestLine[1]);
			log.debug("resource: {}", resource);
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
				log.debug("host address: {}", hostAddress);
				hostPort = Integer.parseInt(hostParts[1]);
				log.debug("host port: {}", hostPort);
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
				log.debug("header line: '{}'", line);
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
	public void close() {
		
		try {
			client.close();
		} catch(IOException e) {
			log.error("Could not close client socket.", e);
		} finally {
			scanner.close();
			printer.close();
		}
		log.debug("Closed client socket.");
	}
}
