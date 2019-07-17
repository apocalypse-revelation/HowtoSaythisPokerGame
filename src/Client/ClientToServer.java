package Client;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClientToServer {
	public static void sendMsgToServer(String str, DataOutputStream toServer) throws IOException {
		toServer.write(str.getBytes());
	}
}
