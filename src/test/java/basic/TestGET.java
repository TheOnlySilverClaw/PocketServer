package basic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.URI;

/**
 * 
 * @author joel
 *
 */
public class TestGET {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final URI target = URI.create("http://127.0.0.1:8080");
	
	private HttpClient client;
	
	@Before
	public void setUp() throws Exception {
		client = HttpClient.getDefault();
	}

	/**
	 * See if a request to the domain root succeeds.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testRootAvailable() throws IOException, InterruptedException {
		
		client.request(target).GET().response();
	}
	
	@Test
	public void testParam() throws IOException, InterruptedException {
		client.request(target.resolve("?name=test")).GET().response();
	}

}
