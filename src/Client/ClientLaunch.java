package Client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientLaunch {
	
	public static void main(String[] args) throws UnknownHostException {
		//
		try {
			ClientThreadToFromServer launchClientUi = new ClientThreadToFromServer("localhost", Integer.toString(new Random().nextInt(999)) );
			new Thread(launchClientUi).start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
