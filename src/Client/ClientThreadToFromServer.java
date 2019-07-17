package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientThreadToFromServer extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static BeanPlayer[] player = new BeanPlayer[5];// ����б�
	public static int LocalNumber;// ����������

	public static int whoBoss;
	public static int lastTakeNum;// �ϴγ���������

	public Container container = null;// ��������
	public JMenuItem exit, replay, about;// ����˵���ť
	public static JButton scrambleLord_btn[] = new JButton[2];// ��ׯ/��Ҫ��ť
	public static JButton bringOutCard_btn[] = new JButton[2];// ���ư�ť
	public static JComboBox<String> selectPattern_Jbox = new JComboBox<String>();//ѡ��ɫ��ť
	public static JButton castCards_btn = new JButton("����");
	public int pattern = 1;//ѡ��ɫ��Ĭ��Ϊ����=1
	public static int whoWon = 0;
	public static int staticWhoWon = 0;
	public static int f = 1;
	
	
	JLabel wait;
	JLabel main;
	public static JLabel clock[] = new JLabel[5];
	JLabel[] playerScore = new JLabel[4];// ��ҵ÷ֱ�ǩ
	JLabel[] playPhoto = new JLabel[4];// ���ͷ��
	JLabel[] cardsWest = new JLabel[33];// ������
	JLabel[] cardsNorth = new JLabel[33];// ������
	JLabel[] cardsEast = new JLabel[33];// ������
	static List<BeanCard> bossCards = new ArrayList<>();// ������
	List<BeanCard> myCardPuts_List = new ArrayList<BeanCard>();// �Լ�����
	static List<BeanCard> lastCardPuts_List = new ArrayList<>();// �ϼҳ���
	static List<BeanCard> holderPutFirstCardList = new ArrayList<>();//ׯ����һ�η�����
	static List<BeanCard> winnerPutFirstCardList = new ArrayList<>();//Ӯ�ҷ���
	JLabel[] dizhuJLabels = new JLabel[8];//8�ſ���ͼ��
	static int holderFlag = 1;
	static int civilianFlag = 0;
	static int civilianFlag2 = 0;

	String serverIP;
	String playerName;
	Socket socket;

	
	public ClientThreadToFromServer(String serverIP, String playername) throws UnknownHostException, IOException 
	{
		this.serverIP = serverIP;
		this.playerName = playername;
		this.socket = new Socket(serverIP, 8888);
		this.setTitle("��������һ" );
		this.setSize(1200, 700);
		setResizable(false);
		wait = new JLabel(new ImageIcon("images/backgroundimg.jpg"));
		wait.setSize(300, 110);
		wait.setVisible(true);
		this.add(wait);
		setLocationRelativeTo(getOwner());
		
		for (int i = 1; i <= 4; i++) 
		{
			player[i] = new BeanPlayer("δ����", 0);
			player[i].cardList = new ArrayList<BeanCard>();
		}
		this.setVisible(true);
	}

	/*
	 * �����沼��:�̶�λ�� ����������
	 */
	public void init() 
	{

		wait.setVisible(false);
		setLocationRelativeTo(getOwner()); // ��Ļ����
		container = this.getContentPane();
		container.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setBackground(new Color(255, 255, 255)); // ����Ϊ��ɫ
//		main = new JLabel(new ImageIcon("images/mainbg.jpg"));
//		main.setSize(1200, 700);
//		main.setVisible(true);
//		container.add(main);
//		main.setOpaque(true);

	}

	//~~~~~~~~~~~~~~~~~~~~�ķ����֣���������-0231
	// ���ö�����Ҳ���
	private void setEast() 
	{
		// ������ҵ÷�
		playerScore[0] = new JLabel(Integer.toString(player[getEastNum()].getScore())); 
		playerScore[0].setBounds(1100, 270, 80, 30);
		playerScore[0].setVisible(true);
		playerScore[0].setBackground(new Color(255, 255, 255));
		this.add(playerScore[0]);
		// �������ͷ��
		playPhoto[0] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[0].setBounds(1100, 300, 80, 70);
		playPhoto[0].setVisible(true);
		this.add(playPhoto[0]);
		upEast();
	}
	// ���ñ�����Ҳ���
	private void setNorth() {
//		playerScore[2] = new JLabel(player[getNorthNum()].getName()); // δ��ȡ�������
		// ������ҵ÷�
		playerScore[2] = new JLabel(Integer.toString(player[getNorthNum()].getScore()));
		playerScore[2].setBounds(820, 105, 80, 30);
		playerScore[2].setVisible(true);
		playerScore[2].setBackground(new Color(255, 255, 255));
		this.add(playerScore[2]);
		// �������ͷ��
		playPhoto[2] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[2].setBounds(820, 35, 80, 70);
		playPhoto[2].setVisible(true);
		this.add(playPhoto[2]);
		upNorth();

	}

	// ����������Ҳ���
	private void setWest() {
		// �����������
		playerScore[3] = new JLabel(Integer.toString(player[getWestNum()].getScore()));
		playerScore[3].setBounds(20, 270, 80, 30);
		playerScore[3].setVisible(true);
		playerScore[3].setBackground(new Color(255, 255, 255));
		this.add(playerScore[3]);
		// �������ͷ��
		playPhoto[3] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[3].setBounds(20, 300, 80, 70);
		playPhoto[3].setVisible(true);
		this.add(playPhoto[3]);
		// �������߿���λ��
		upWest();

	}

	// �����м����
	private void setCenter() {
		for (int i = dizhuJLabels.length - 1; i >= 0; i--) {
			dizhuJLabels[i] = new JLabel(new ImageIcon("images/rear.gif"));
			dizhuJLabels[i].setBounds(400 + i * 25, 275, 71, 96);
			dizhuJLabels[i].setVisible(true);
			this.add(dizhuJLabels[i]);
		}
	}

	// �������汾����Ҳ���
	private void setSouth() 
	{
		// �����������
		playerScore[1] = new JLabel(Integer.toString(player[LocalNumber].getScore())); // ��ȡ�������
		playerScore[1].setBounds(200, 610, 80, 30);
		playerScore[1].setVisible(true);
		playerScore[1].setBackground(new Color(255, 255, 255));
		this.add(playerScore[1]);
		// �������ͷ��
		playPhoto[1] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[1].setBounds(200, 540, 80, 70);
		playPhoto[1].setVisible(true);
		this.add(playPhoto[1]);
		// �����ϱ߿���λ��
		for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) {
			BeanCard a = player[LocalNumber].cardList.get(i);
			// a.canClick = true;
			this.add(a);
			a.setLocation(300 + i * 15, 540);
		}

	}

	// �������汾����Ҳ���
	private void setLocal() 
	{
		// ��ť
		scrambleLord_btn[0] = new JButton("��     ׯ");
		scrambleLord_btn[1] = new JButton("��     Ҫ");
		bringOutCard_btn[0] = new JButton("����");
//		bringOutCard_btn[1] = new JButton("��Ҫ");
		//ѡ��ɫ������
		selectPattern_Jbox.removeAllItems();//��ֹ������ׯ��ʱ������item
		selectPattern_Jbox.addItem("����");
		selectPattern_Jbox.addItem("����");
		selectPattern_Jbox.addItem("�ݻ�");
		selectPattern_Jbox.addItem("����");
		//���ư�ť
		castCards_btn.setBounds(350,500,60,20);
		container.add(castCards_btn);
		castCards_btn.setVisible(false);
		//��ׯ/����
		bringOutCard_btn[0].setBounds(450 + 100, 500, 60, 20);
		bringOutCard_btn[0].setVisible(false);
		container.add(bringOutCard_btn[0]);
		for (int i = 0; i < 2; i++) 
		{
			scrambleLord_btn[i].setBounds(450 + i * 100, 500, 75, 20);
			container.add(scrambleLord_btn[i]);
			scrambleLord_btn[i].setVisible(false);
			
			
		}
		//ѡ��ɫ
		selectPattern_Jbox.setBounds(650 ,500, 90, 20);
		container.add(selectPattern_Jbox);
		selectPattern_Jbox.setVisible(false);
		
		// ��ׯ��ť������
		scrambleLord_btn[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//ȷ��ׯ��
					JSONObject json = new JSONObject();
					json.put("type", 1);
					json.put("pnum", LocalNumber);
					json.put("msg", "yes");
					//���ͷ�����
//					ToServer.sendMsg(json0.toString(), new DataOutputStream(socket.getOutputStream()));
					ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				selectPattern_Jbox.setVisible(false);
				for (int i = 0; i < 2; i++) {
					scrambleLord_btn[i].setVisible(false);
				}
				for (int i = dizhuJLabels.length - 1; i >= 0; i--) {
					dizhuJLabels[i].setVisible(false);
				}
				//����ʾ���ư�ť��
				castCards_btn.setVisible(true);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for (BeanCard a : bossCards) 
				{
					player[LocalNumber].cardList.add(a);
				}

				// �õ������ƺ� ��������������Ϊδ���״̬
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
				{
					player[LocalNumber].cardList.get(i).clicked = false;
				}
				
				//Ĭ��Ϊ����
				pattern = 1;
				if(selectPattern_Jbox.getSelectedItem()=="����")
					pattern = 1;
				else if(selectPattern_Jbox.getSelectedItem()=="����")
					pattern = 2;
				else if(selectPattern_Jbox.getSelectedItem()=="�ݻ�")
					pattern = 3;
				else if(selectPattern_Jbox.getSelectedItem()=="����")
					pattern = 4;
				//����������˳��
				CardFunction.cardListSort(player[LocalNumber].cardList,pattern);

				for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) {
					BeanCard a = player[LocalNumber].cardList.get(i);
					container.add(a);
					a.setLocation(300 + i * 15, 540);
				}

				// ���ĵ���ͷ��
				playPhoto[1].setIcon(new ImageIcon("images/dizhu.png"));
			}
		});
		//���ư�ť����
		castCards_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// �����������Ʒ���list ÿ�ε������ո�list
				List<BeanCard> temp = new ArrayList<BeanCard>();
				myCardPuts_List.clear();
				//���ƣ�������е�������������ûƨ�ã�ֻ�ܷ�2�ţ�
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
				{
					BeanCard a = player[LocalNumber].cardList.get(i);
					if (a.clicked) //�Ʊ����
					{
						myCardPuts_List.add(a);
					}
				}
				//������ʱlist
				temp.addAll(myCardPuts_List);

				int count = 0;
				if (CardFunction.eligibleCast(myCardPuts_List)) 
				{
					//
					for (BeanCard a : myCardPuts_List) {
						a.setVisible(false);
						a.canClick = false;// ���ܷ���
						count++;
					}
					//�������ư�ť
					castCards_btn.setVisible(false);
					//��ʾ���ư�ť
					bringOutCard_btn[0].setVisible(true);
					//�����ƣ��ӳ��������������
					player[LocalNumber].cardList.removeAll(temp);
				}
				// ���ƺ���������
				reCardList();
				//�ѵ�����ƽ���
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
					player[LocalNumber].cardList.get(i).clicked = false;
			}
		});
		// ��Ҫׯ����ť������
		scrambleLord_btn[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					JSONObject json = new JSONObject();
					json.put("type", 1);
					json.put("pnum", LocalNumber);
					json.put("msg", "no");
					ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				selectPattern_Jbox.setVisible(false);
				for (int i = 0; i < 2; i++) {
					scrambleLord_btn[i].setVisible(false);
				}
			}
		});

		// ���ư�ť
		bringOutCard_btn[0].addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				List<BeanCard> temp = new ArrayList<BeanCard>();
				myCardPuts_List.clear();
				//���ƣ�������е�������������ûƨ�ã�ֻ�ܷ�2�ţ�
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
				{
					BeanCard a = player[LocalNumber].cardList.get(i);
					if (a.clicked) //�Ʊ����
						myCardPuts_List.add(a);
				}
				//������ʱlist
				temp.addAll(myCardPuts_List);
				int count = 0;
				//�жϵ�����Ƶ��ǲ���ׯ����3���˶���ׯ����һ�η���Ϊ׼
				//1111111111111�жϵ�һ��ׯ��
				System.out.println("~~~~~��һ�ֿ�ʼ");
//				System.out.println("�Ƿ�ׯ��:"+(whoBoss==LocalNumber));
				System.out.println("����:"+CardFunction.judgeHolder(myCardPuts_List));
				System.out.println("size:"+winnerPutFirstCardList.size());
				if(f % 4 == 1 &&CardFunction.judgeHolder(myCardPuts_List))
				{
					System.out.println("����1");
					holderFlag = 0;//ֻ�ж�1��(�������û�ã���
					for (BeanCard a : myCardPuts_List) {
						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
						a.canClick = false;// ���ܷ���
						count++;
					}
					// �رճ��ư�ť
					bringOutCard_btn[0].setVisible(false);

					// ������������Ϊһ���ַ���
					String putCards = "";
					for (BeanCard c : myCardPuts_List) {
						putCards += c.getName() + " ";
					}
					//������Ϣtype2����������
					try {
						JSONObject json = new JSONObject();
						json.put("type", 2);
						json.put("pnum", LocalNumber);
						json.put("msg", putCards.trim());
						ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
					} catch (JSONException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//�����ƣ��ӳ��������������
					player[LocalNumber].cardList.removeAll(temp);
					//û�ƣ���Ϸ������Ϣ����������
					if (player[LocalNumber].cardList.size() == 0) 
					{
						try {
							JSONObject json = new JSONObject();
							json.put("type", 3);
							json.put("pnum", LocalNumber);
							ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
						} catch (JSONException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				//222222222222222�жϵ�һ������ũ��
				System.out.println("~~~~~�ڶ��ֿ�ʼ");
//				System.out.println("ׯ��������"+holderPutFirstCardList.size());
				System.out.println("size:"+winnerPutFirstCardList.size());
//				System.out.println("�Ƿ�ũ��"+(whoBoss!=LocalNumber));
//				System.out.println("~~~~~civilianFlag2:>=3?:"+(civilianFlag2<3));
////				System.out.println("civil��"+(civilianFlag == 1));
				System.out.println("����:"+CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList));
				if(f % 4 != 1 &&CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList))
				{
					System.out.println("����2");
					for (BeanCard a : myCardPuts_List) 
					{
						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
						a.canClick = false;// ���ܷ���
						count++;
					}
				 
					// �رճ��ư�ť
					bringOutCard_btn[0].setVisible(false);

					// ������������Ϊһ���ַ���
					String putCards = "";
					for (BeanCard c : myCardPuts_List) {
						putCards += c.getName() + " ";
					}
					//������Ϣtype2����������
					try {
						JSONObject json = new JSONObject();
						json.put("type", 2);
						json.put("pnum", LocalNumber);
						json.put("msg", putCards.trim());
						ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
					} catch (JSONException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//�����ƣ��ӳ��������������
					player[LocalNumber].cardList.removeAll(temp);
					//û�ƣ���Ϸ������Ϣ����������
					if (player[LocalNumber].cardList.size() == 0) 
					{
						try {
							JSONObject json = new JSONObject();
							json.put("type", 3);
							json.put("pnum", LocalNumber);
							ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
						} catch (JSONException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
//				System.out.println("~~~~~�����ֿ�ʼ");
//				System.out.println("Ӯ��������"+winnerPutFirstCardList.size());
//				System.out.println("~~~~~civilianFlag2:>=3?:"+(civilianFlag2<3));
//				System.out.println("���ͣ�"+CardFunction.judgeHolder(myCardPuts_List));
//				System.out.println("whoWon�ǲ��Ǳ��أ�"+(staticWhoWon == LocalNumber));
//				System.out.println("whoWonֵ��"+staticWhoWon);
//				System.out.println("holderFlag =="+holderFlag );
//				//3333333333333333�ж�Ӯ�����(��ׯ�ҵ��жϺ���һ�£�
//				if(holderFlag == 0 &&civilianFlag2 >= 3&& staticWhoWon == LocalNumber && CardFunction.judgeHolder(myCardPuts_List))
//				{
//					
//					System.out.println("����3");
//					for (BeanCard a : myCardPuts_List) {
//						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
//						a.canClick = false;// ���ܷ���
//						count++;
//					}
//					// �رճ��ư�ť
//					bringOutCard_btn[0].setVisible(false);
//
//					// ������������Ϊһ���ַ���
//					String putCards = "";
//					for (BeanCard c : myCardPuts_List) {
//						putCards += c.getName() + " ";
//					}
//					//������Ϣtype2����������
//					try {
//						JSONObject json = new JSONObject();
//						json.put("type", 2);
//						json.put("pnum", LocalNumber);
//						json.put("msg", putCards.trim());
//						ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
//					} catch (JSONException e1) {
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					//�����ƣ��ӳ��������������
//					player[LocalNumber].cardList.removeAll(temp);
//					//û�ƣ���Ϸ������Ϣ����������
//					if (player[LocalNumber].cardList.size() == 0) 
//					{
//						try {
//							JSONObject json = new JSONObject();
//							json.put("type", 3);
//							json.put("pnum", LocalNumber);
//							ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
//						} catch (JSONException e1) {
//							e1.printStackTrace();
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//				
//				System.out.println("~~~~~�����ֿ�ʼ\n");
//				System.out.println("Ӯ��������"+winnerPutFirstCardList.size());
//				System.out.println("���ͣ�"+CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList));
//				System.out.println("whoWon���Ǳ��أ�"+(staticWhoWon != LocalNumber));
//				System.out.println("whoWonֵ��"+staticWhoWon);
//				//��Ӯ�� ,��ʱ��Ӯ���ƱȽ�
//				if(civilianFlag2 >= 3 && holderFlag == 0 && staticWhoWon != LocalNumber&&CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList))
//				{
//					System.out.println("����4");
//					for (BeanCard a : myCardPuts_List) {
//						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
//						a.canClick = false;// ���ܷ���
//						count++;
//					}
//					// �رճ��ư�ť
//					bringOutCard_btn[0].setVisible(false);
//
//					// ������������Ϊһ���ַ���
//					String putCards = "";
//					for (BeanCard c : myCardPuts_List) {
//						putCards += c.getName() + " ";
//					}
//					//������Ϣtype2����������
//					try {
//						JSONObject json = new JSONObject();
//						json.put("type", 2);
//						json.put("pnum", LocalNumber);
//						json.put("msg", putCards.trim());
//						ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
//					} catch (JSONException e1) {
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					//�����ƣ��ӳ��������������
//					player[LocalNumber].cardList.removeAll(temp);
//					//û�ƣ���Ϸ������Ϣ����������
//					if (player[LocalNumber].cardList.size() == 0) 
//					{
//						try {
//							JSONObject json = new JSONObject();
//							json.put("type", 3);
//							json.put("pnum", LocalNumber);
//							ClientToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
//						} catch (JSONException e1) {
//							e1.printStackTrace();
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//				if (CardCtrl.cardListCompare(myCardPuts_List, lastCardPuts_List,player[LocalNumber].cardList)) 
//				{
//					//�ϼ�����ʧ
//					for (Card a : myCardPuts_List) {
//						CardCtrl.move(a, getLocation(), new Point(400 + count * 15, 275));
//						a.canClick = false;// ���ܷ���
//						count++;
//					}
//
//					// �رճ��ư�ť
//					bringOutCard_btn[0].setVisible(false);
//
//					// ������������Ϊһ���ַ���
//					String putCards = "";
//					for (Card c : myCardPuts_List) {
//						putCards += c.getName() + " ";
//					}
//					//������Ϣtype2����������
//					try {
//						JSONObject json = new JSONObject();
//						json.put("type", 2);
//						json.put("pnum", LocalNumber);
//						json.put("msg", putCards.trim());
//						ToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
//					} catch (JSONException e1) {
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					//�����ƣ��ӳ��������������
//					player[LocalNumber].cardList.removeAll(temp);
//					//û�ƣ���Ϸ������Ϣ����������
//					if (player[LocalNumber].cardList.size() == 0) 
//					{
//						try {
//							JSONObject json = new JSONObject();
//							json.put("type", 3);
//							json.put("pnum", LocalNumber);
//							ToServer.sendMsgToServer(json.toString(), new DataOutputStream(socket.getOutputStream()));
//						} catch (JSONException e1) {
//							e1.printStackTrace();
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
				// ���ƺ���������
				reCardList();
				//�ѵ�����ƽ���
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
					player[LocalNumber].cardList.get(i).clicked = false;
			}
		});
	}

	// ���ƺ���������
	protected void reCardList() 
	{
		for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) 
		{
			BeanCard a = player[LocalNumber].cardList.get(i);
			this.add(a);
			a.setBounds(300 + i * 15, 540, 71, 96);
		}
	}
	// ���±���������
	public void getLocalPlayer(BeanPlayer[] playerList) {
		for (int i = 1; i <= 4; i++) {
			if (playerList[i].getName().equals(playerName)) {
				LocalNumber = i;
			}
		}
	}
	public void upWest() 
	{
		for (int i = 0; i < cardsWest.length; i++) 
		{
			if (cardsWest[i] != null)
				cardsWest[i].setVisible(false);
		}
		for (int i = 0; i < player[getWestNum()].cardList.size(); i++) 
		{
			cardsWest[i] = new JLabel(new ImageIcon("images/rear.gif"));
			cardsWest[i].setBounds(110, 150 + i * 10, 71, 96);
			cardsWest[i].setVisible(true);
			this.add(cardsWest[i]);
		}
	}

	public void upEast() 
	{
		// ���Ö|�߿���λ��
		for (int i = 0; i < cardsEast.length; i++) {
			if (cardsEast[i] != null)
				cardsEast[i].setVisible(false);
		}

		for (int i = 0; i < player[getEastNum()].cardList.size(); i++) {
			cardsEast[i] = new JLabel(new ImageIcon("images/rear.gif"));
			cardsEast[i].setBounds(1020, 150 + i * 10, 71, 96);
			cardsEast[i].setVisible(true);
			this.add(cardsEast[i]);
		}
	}

	public void upNorth() {
		for (int i = 0; i < cardsNorth.length; i++) {
			if (cardsNorth[i] != null)
				cardsNorth[i].setVisible(false);
		}
		// ���ñ��߿���λ��
		for (int i = 0; i < player[getNorthNum()].cardList.size(); i++) {
			cardsNorth[i] = new JLabel(new ImageIcon("images/rear.gif"));
			cardsNorth[i].setBounds(730 - i * 15, 35, 71, 96);
			cardsNorth[i].setVisible(true);
			this.add(cardsNorth[i]);
		}
	}

	public void upLastPuts() 
	{
		for (int i = lastCardPuts_List.size() - 1; i >= 0; i--) {
			lastCardPuts_List.get(i).setBounds(400 + i * 15, 275, 71, 96);
			lastCardPuts_List.get(i).setVisible(true);
			this.add(lastCardPuts_List.get(i));
		}
	}

	public void upExceptLocal() {
		// ���³�ȥ�����������������ҵ�����
		upWest();
		upEast();
		upNorth();
	}

	// ��ø�����λ��ҵĺ���
	public int getEastNum() {
		return (LocalNumber + 1) > 4 ? (LocalNumber + 1 - 4) : (LocalNumber + 1);
	}

	public int getWestNum() {
		return (LocalNumber + 3) > 4 ? (LocalNumber + 3 - 4) : (LocalNumber + 3);
	}

	public int getNorthNum() {
		return (LocalNumber + 2) > 4 ? (LocalNumber + 2 - 4) : (LocalNumber + 2);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~�ͻ����߳�~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public void run() {
		try {
			DataInputStream fromServer = new DataInputStream(socket.getInputStream());
			DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
			toServer.write(playerName.getBytes());//����������������ִ��Ͷ���������
			while (true) 
			{
				repaint();
				byte[] b = new byte[3000];
				fromServer.read(b);
				String msg = new String(b).trim();
				//System.out.println("����˷�����" + msg);
				//��
				JSONObject json = new JSONObject(msg);
				int commandType = json.getInt("type");
				//type��				pnum��������		msg������+�Ƶ�
//				1.ȷ��ׯ��
//				2.������Ϣ
//				3.������Ϣ
//				4.��Ϸ����
//				5.������/����
//				6.���8�ſ���
//				7.ѯ���Ƿ��ׯ����
//				8.�Ʒ�+ȷ����ʤ��
				switch (commandType) 
				{
				//ȷ��ÿһ�ֵĻ�ʤ�ߣ����ӷ�
				//sendRoundWin();
				case 8:
					int winPlayer = json.getInt("pnum");
					int getScore = json.getInt("msg");
					whoWon = winPlayer;//��ǻ�ʤ��
					//
					//operatingNum = winPlayer;
					player[winPlayer].setScore(player[winPlayer].getScore()+getScore);
					playerScore[0].setText(Integer.toString(player[getEastNum()].getScore()));
					playerScore[1].setText(Integer.toString(player[LocalNumber].getScore()));
					playerScore[2].setText(Integer.toString(player[getNorthNum()].getScore()));
					playerScore[3].setText(Integer.toString(player[getWestNum()].getScore()));
					break;
				case 1:
					// �յ�ȷ��ׯ����� end
					for (int i = dizhuJLabels.length - 1; i >= 0; i--) 
					{
						dizhuJLabels[i].setVisible(false);
					}
					player = PlayerFunction.determineBoss(json, player, LocalNumber);
					//ׯ�������
					int n = json.getInt("pnum");
					//operatingNum = n;
					for (int i = 1; i <= 4; i++) 
					{
						if (player[i].playNumber == n) 
						{
							if (i == ((LocalNumber + 3) > 4 ? (LocalNumber - 1) : (LocalNumber + 3)))
								playPhoto[3].setIcon(new ImageIcon("images/dizhu.png"));
							if (i == ((LocalNumber + 2) > 4 ? (LocalNumber - 2) : (LocalNumber + 2)))
								playPhoto[2].setIcon(new ImageIcon("images/dizhu.png"));
							if (i == ((LocalNumber + 1) > 4 ? (LocalNumber - 3) : (LocalNumber + 1)))
								playPhoto[0].setIcon(new ImageIcon("images/dizhu.png"));
						}
					}
					break;
				case 2:
					// �յ�����
					int num = json.getInt("pnum");
					if (!json.getString("msg").equals("0")) {
						lastTakeNum = num;
						for (BeanCard a : lastCardPuts_List) {
							a.setVisible(false);
						}
						lastCardPuts_List = PlayerFunction.takeCards(json);
						if(f++ % 4  == 1)
						{
							winnerPutFirstCardList.clear();
							winnerPutFirstCardList.addAll(lastCardPuts_List);
						}
//						//ȷ�ϵ�һ��ׯ��
//						if(holderFlag == 1&&num == whoBoss)
//						{
//							holderPutFirstCardList.addAll(lastCardPuts_List);
//							holderFlag = 0;//�����ж�ׯ��
//							civilianFlag = 1;//��ʼ�жϵ�һ���������
//						}
//						//ȷ�ϵ�һ�������������
//						if(civilianFlag2 < 3&&civilianFlag == 1 && num!=whoBoss)
//						{
//							civilianFlag2++;//����3�ιر��жϣ�����Ӯ�һ��ڣ�
//							if(whoWon>0)
//							{
//								staticWhoWon = whoWon;//���һ��Ҫȷ��Ӯ�ҡ�
//								f = 1;
//							}
//						}
//						if(num == staticWhoWon)
//						{
//							f =0;
//						}
//						//ȷ��Ӯ�ҵ�һ�η���(������ܹرգ�һֱѭ���жϣ�
//						if(f!=1&&civilianFlag2 >= 3&&num == staticWhoWon)//whoWon�Ѿ��������ˣ�����static
//						{
//							winnerPutFirstCardList.clear();
//							winnerPutFirstCardList.addAll(lastCardPuts_List);//
//							staticWhoWon = 0;
//						}
//						
//						if(civilianFlag2 >= 3&&whoWon > 0)//whoWon�Ѿ��������ˣ�����static
//						{
//							staticWhoWon = whoWon;//����ҲҪһֱ�жϣ�
//						}
							
						if (num != LocalNumber) 
						{
							for (int i = 0; i < lastCardPuts_List.size(); i++) {
								player[num].cardList.remove(0);
							}
						}
						for (BeanCard a : myCardPuts_List) {
							a.setVisible(false);
						}
					}
					upLastPuts();
					upExceptLocal();
//					if(num == )
					//˳ʱ���ж�
					num = num + 1;
					if (num > 4)
						num -= 4;//�����4����ҽ�����Ӧ�ôӵ�1����ʼ������5-4��
					System.out.println("whoWOn:"+whoWon);
//������һ���жϣ�ÿһ�ֻ�û�л�ʤ�ߵ�ʱ��˳ʱ��ѭ�����л�ʤ�ߵ�ʱ�򣬻�ʤ���г���Ȩ���ӻ�ʤ�߿�ʼ˳ʱ�����
					if(whoWon == 0)//û��ʤ��ʱ��4����û���꣩��˳ʱ����ʾ
					{
						if (num == LocalNumber) 
							bringOutCard_btn[0].setVisible(true);
					}
					else 
						if(whoWon == LocalNumber)//�л�ʤ��ʱ����
							bringOutCard_btn[0].setVisible(true);
					whoWon = 0;	//һ�غϽ���Ҫ���㣬�ȴ�case8
					break;
				case 3:// �յ����� end
					player = PlayerFunction.releaseCards(json, player, LocalNumber);
					for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) 
					{
						BeanCard a = player[LocalNumber].cardList.get(i);
						// a.canClick = true;
						this.add(a);
						a.setLocation(300 + i * 15, 540);
					}
					break;
				case 4:// �յ���Ϸ����
					int VictoryNum = json.getInt("pnum");
					String VictoryCamp = player[VictoryNum].isBoss ? "����" : "ũ��";
					GameOver.show(player[VictoryNum].name + "������~\n" + VictoryCamp + "���ʤ��~");
					this.setVisible(false);
					break;

				case 5:// �յ������ż����� end
					player = PlayerFunction.getLocalPlayer(json, player);
					getLocalPlayer(player);// ���±���������-LocalNumber

					for (int i = 1; i <= 4; i++) {
						if (i != LocalNumber) {
							for (int j = 1; j <= 25; j++) {
								BeanCard a = new BeanCard("1-1", false);
								a.canClick = false;
								player[i].cardList.add(a);
							}
						}
					}
					break;
				case 6:// �յ�ׯ���� end
					bossCards = PlayerFunction.getBOssCards(json, bossCards);
					break;
				case 7:// �յ�ѯ��ׯ������ end
					whoBoss = json.getInt("pnum");
					//operatingNum = whoBoss;
					init();// ��ʼ�������棬���ó���ߣ�������ɫ��
					setWest();// ���ö�����ҵ�ͷ�������Լ�����λ�ã���Ҫ��̬��ȡ���������ͷ��
					setNorth();// ���ñ�����ҵ�ͷ�������Լ�����λ�ã���Ҫ��̬��ȡ���������ͷ��
					setEast();// �����ϱ���ҵ�ͷ�������Լ�����λ�ã���Ҫ��̬��ȡ���������ͷ��
					setCenter();// ���������м�ׯ������ʾ
					setSouth();// �������棨���أ���ҵ�������ͷ���Լ���ʾ������ҵ�����
					setLocal();// �������棨���أ���ҵİ�ť��λ�ã�Ĭ��Ϊ���ɼ���
					if (whoBoss == LocalNumber) {
						for (int i = 0; i < 2; i++) {
							scrambleLord_btn[i].setVisible(true);
						}
						selectPattern_Jbox.setVisible(true);
					}
					break;
				default:
					System.out.println("json���ݲ��Ϸ�");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

class GameOver {
	public static void show(String text) {
		JDialog dialog = new JDialog();
		JLabel label = new JLabel(text, JLabel.CENTER);
		dialog.add(label);
		dialog.setSize(300, 110);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.setResizable(true);
	}
}
