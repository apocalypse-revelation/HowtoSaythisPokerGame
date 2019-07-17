package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerLaunch {

	public static void main(String[] args) {

		try {
			Socket[] socket = new Socket[5];
			DataOutputStream[] toClient = new DataOutputStream[5];
			DataInputStream[] fromClient = new DataInputStream[5];
			Thread[] thread = new Thread[5];
			int n = 1;
			ServerSocket serversocket = new ServerSocket(8888);
			String[] playernames = new String[5];
			while (true) //while-这里我主要是开启监听
			{
				socket[n] = serversocket.accept();
				toClient[n] = new DataOutputStream(socket[n].getOutputStream());
				fromClient[n] = new DataInputStream(socket[n].getInputStream());
				System.out.println("序号"+n+"加入");
				byte[] b = new byte[1024];
				fromClient[n].read(b);
				playernames[n] = new String(b).trim();
				System.out.println(playernames[n]);
				n++;
				if (n == 5) {
					break;
				}
			}
			ServerFunctionToClient serverfunctionToclient = new ServerFunctionToClient(socket, toClient, fromClient);
			for (int i = 1; i <= 4; i++) 
			{
				thread[i] = new Thread(new SocketThreadFromClient(serverfunctionToclient, fromClient[i]));
			}
			String playername = playernames[1] + " " + playernames[2] + " " + playernames[3] + " " + playernames[4];
			serverfunctionToclient.setPlayer(playername);
			//询问庄主
			Random rand = new Random();
			int ran = rand.nextInt(4) + 1;
			serverfunctionToclient.sendBossMsg(ran);

			for(int k = 1; k <= 4; k++)
			{
				thread[k].start();
			}
			
			serverfunctionToclient.sendCards();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}