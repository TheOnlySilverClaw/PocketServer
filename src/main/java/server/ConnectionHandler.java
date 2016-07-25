package server;

import java.io.IOException;
import java.net.Socket;

public interface ConnectionHandler {

	Connection handle(Socket connection) throws IOException;
}
