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
	public static BeanPlayer[] player = new BeanPlayer[5];// 玩家列表
	public static int LocalNumber;// 本地玩家序号

	public static int whoBoss;
	public static int lastTakeNum;// 上次出牌玩家序号

	public Container container = null;// 定义容器
	public JMenuItem exit, replay, about;// 定义菜单按钮
	public static JButton scrambleLord_btn[] = new JButton[2];// 叫庄/不要按钮
	public static JButton bringOutCard_btn[] = new JButton[2];// 出牌按钮
	public static JComboBox<String> selectPattern_Jbox = new JComboBox<String>();//选花色按钮
	public static JButton castCards_btn = new JButton("弃牌");
	public int pattern = 1;//选花色，默认为黑桃=1
	public static int whoWon = 0;
	public static int staticWhoWon = 0;
	public static int f = 1;
	
	
	JLabel wait;
	JLabel main;
	public static JLabel clock[] = new JLabel[5];
	JLabel[] playerScore = new JLabel[4];// 玩家得分标签
	JLabel[] playPhoto = new JLabel[4];// 玩家头像
	JLabel[] cardsWest = new JLabel[33];// 西边牌
	JLabel[] cardsNorth = new JLabel[33];// 北边牌
	JLabel[] cardsEast = new JLabel[33];// 东边牌
	static List<BeanCard> bossCards = new ArrayList<>();// 地主牌
	List<BeanCard> myCardPuts_List = new ArrayList<BeanCard>();// 自己出牌
	static List<BeanCard> lastCardPuts_List = new ArrayList<>();// 上家出牌
	static List<BeanCard> holderPutFirstCardList = new ArrayList<>();//庄主第一次发的牌
	static List<BeanCard> winnerPutFirstCardList = new ArrayList<>();//赢家发牌
	JLabel[] dizhuJLabels = new JLabel[8];//8张扣牌图案
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
		this.setTitle("红五三打一" );
		this.setSize(1200, 700);
		setResizable(false);
		wait = new JLabel(new ImageIcon("images/backgroundimg.jpg"));
		wait.setSize(300, 110);
		wait.setVisible(true);
		this.add(wait);
		setLocationRelativeTo(getOwner());
		
		for (int i = 1; i <= 4; i++) 
		{
			player[i] = new BeanPlayer("未连接", 0);
			player[i].cardList = new ArrayList<BeanCard>();
		}
		this.setVisible(true);
	}

	/*
	 * 主界面布局:固定位置 主界面设置
	 */
	public void init() 
	{

		wait.setVisible(false);
		setLocationRelativeTo(getOwner()); // 屏幕居中
		container = this.getContentPane();
		container.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setBackground(new Color(255, 255, 255)); // 背景为绿色
//		main = new JLabel(new ImageIcon("images/mainbg.jpg"));
//		main.setSize(1200, 700);
//		main.setVisible(true);
//		container.add(main);
//		main.setOpaque(true);

	}

	//~~~~~~~~~~~~~~~~~~~~四方布局，东北西南-0231
	// 设置东边玩家布局
	private void setEast() 
	{
		// 设置玩家得分
		playerScore[0] = new JLabel(Integer.toString(player[getEastNum()].getScore())); 
		playerScore[0].setBounds(1100, 270, 80, 30);
		playerScore[0].setVisible(true);
		playerScore[0].setBackground(new Color(255, 255, 255));
		this.add(playerScore[0]);
		// 设置玩家头像
		playPhoto[0] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[0].setBounds(1100, 300, 80, 70);
		playPhoto[0].setVisible(true);
		this.add(playPhoto[0]);
		upEast();
	}
	// 设置北边玩家布局
	private void setNorth() {
//		playerScore[2] = new JLabel(player[getNorthNum()].getName()); // 未获取玩家姓名
		// 设置玩家得分
		playerScore[2] = new JLabel(Integer.toString(player[getNorthNum()].getScore()));
		playerScore[2].setBounds(820, 105, 80, 30);
		playerScore[2].setVisible(true);
		playerScore[2].setBackground(new Color(255, 255, 255));
		this.add(playerScore[2]);
		// 设置玩家头像
		playPhoto[2] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[2].setBounds(820, 35, 80, 70);
		playPhoto[2].setVisible(true);
		this.add(playPhoto[2]);
		upNorth();

	}

	// 设置西边玩家布局
	private void setWest() {
		// 设置玩家姓名
		playerScore[3] = new JLabel(Integer.toString(player[getWestNum()].getScore()));
		playerScore[3].setBounds(20, 270, 80, 30);
		playerScore[3].setVisible(true);
		playerScore[3].setBackground(new Color(255, 255, 255));
		this.add(playerScore[3]);
		// 设置玩家头像
		playPhoto[3] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[3].setBounds(20, 300, 80, 70);
		playPhoto[3].setVisible(true);
		this.add(playPhoto[3]);
		// 设置西边卡牌位置
		upWest();

	}

	// 设置中间面板
	private void setCenter() {
		for (int i = dizhuJLabels.length - 1; i >= 0; i--) {
			dizhuJLabels[i] = new JLabel(new ImageIcon("images/rear.gif"));
			dizhuJLabels[i].setBounds(400 + i * 25, 275, 71, 96);
			dizhuJLabels[i].setVisible(true);
			this.add(dizhuJLabels[i]);
		}
	}

	// 设置南面本地玩家布局
	private void setSouth() 
	{
		// 设置玩家姓名
		playerScore[1] = new JLabel(Integer.toString(player[LocalNumber].getScore())); // 获取玩家姓名
		playerScore[1].setBounds(200, 610, 80, 30);
		playerScore[1].setVisible(true);
		playerScore[1].setBackground(new Color(255, 255, 255));
		this.add(playerScore[1]);
		// 设置玩家头像
		playPhoto[1] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[1].setBounds(200, 540, 80, 70);
		playPhoto[1].setVisible(true);
		this.add(playPhoto[1]);
		// 设置南边卡牌位置
		for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) {
			BeanCard a = player[LocalNumber].cardList.get(i);
			// a.canClick = true;
			this.add(a);
			a.setLocation(300 + i * 15, 540);
		}

	}

	// 设置南面本地玩家布局
	private void setLocal() 
	{
		// 按钮
		scrambleLord_btn[0] = new JButton("叫     庄");
		scrambleLord_btn[1] = new JButton("不     要");
		bringOutCard_btn[0] = new JButton("出牌");
//		bringOutCard_btn[1] = new JButton("不要");
		//选花色下拉框
		selectPattern_Jbox.removeAllItems();//防止轮流叫庄的时候无数item
		selectPattern_Jbox.addItem("黑桃");
		selectPattern_Jbox.addItem("红桃");
		selectPattern_Jbox.addItem("草花");
		selectPattern_Jbox.addItem("方块");
		//弃牌按钮
		castCards_btn.setBounds(350,500,60,20);
		container.add(castCards_btn);
		castCards_btn.setVisible(false);
		//叫庄/出牌
		bringOutCard_btn[0].setBounds(450 + 100, 500, 60, 20);
		bringOutCard_btn[0].setVisible(false);
		container.add(bringOutCard_btn[0]);
		for (int i = 0; i < 2; i++) 
		{
			scrambleLord_btn[i].setBounds(450 + i * 100, 500, 75, 20);
			container.add(scrambleLord_btn[i]);
			scrambleLord_btn[i].setVisible(false);
			
			
		}
		//选花色
		selectPattern_Jbox.setBounds(650 ,500, 90, 20);
		container.add(selectPattern_Jbox);
		selectPattern_Jbox.setVisible(false);
		
		// 叫庄按钮监听器
		scrambleLord_btn[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//确认庄主
					JSONObject json = new JSONObject();
					json.put("type", 1);
					json.put("pnum", LocalNumber);
					json.put("msg", "yes");
					//发送服务器
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
				//先显示弃牌按钮；
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

				// 拿到地主牌后 将所有手牌设置为未点击状态
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
				{
					player[LocalNumber].cardList.get(i).clicked = false;
				}
				
				//默认为黑桃
				pattern = 1;
				if(selectPattern_Jbox.getSelectedItem()=="黑桃")
					pattern = 1;
				else if(selectPattern_Jbox.getSelectedItem()=="红桃")
					pattern = 2;
				else if(selectPattern_Jbox.getSelectedItem()=="草花")
					pattern = 3;
				else if(selectPattern_Jbox.getSelectedItem()=="方块")
					pattern = 4;
				//重新排列牌顺序
				CardFunction.cardListSort(player[LocalNumber].cardList,pattern);

				for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) {
					BeanCard a = player[LocalNumber].cardList.get(i);
					container.add(a);
					a.setLocation(300 + i * 15, 540);
				}

				// 更改地主头像
				playPhoto[1].setIcon(new ImageIcon("images/dizhu.png"));
			}
		});
		//弃牌按钮监听
		castCards_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// 将点起来的牌放入list 每次点击先清空该list
				List<BeanCard> temp = new ArrayList<BeanCard>();
				myCardPuts_List.clear();
				//点牌（最多点持有的手牌数，但是没屁用，只能发2张）
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
				{
					BeanCard a = player[LocalNumber].cardList.get(i);
					if (a.clicked) //牌被点击
					{
						myCardPuts_List.add(a);
					}
				}
				//存入临时list
				temp.addAll(myCardPuts_List);

				int count = 0;
				if (CardFunction.eligibleCast(myCardPuts_List)) 
				{
					//
					for (BeanCard a : myCardPuts_List) {
						a.setVisible(false);
						a.canClick = false;// 牌能否点击
						count++;
					}
					//隐藏弃牌按钮
					castCards_btn.setVisible(false);
					//显示出牌按钮
					bringOutCard_btn[0].setVisible(true);
					//扔完牌，从持有牌清除发出的
					player[LocalNumber].cardList.removeAll(temp);
				}
				// 出牌后重新排序
				reCardList();
				//把点起的牌降下
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
					player[LocalNumber].cardList.get(i).clicked = false;
			}
		});
		// 不要庄主按钮监听器
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

		// 出牌按钮
		bringOutCard_btn[0].addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				List<BeanCard> temp = new ArrayList<BeanCard>();
				myCardPuts_List.clear();
				//点牌（最多点持有的手牌数，但是没屁用，只能发2张）
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
				{
					BeanCard a = player[LocalNumber].cardList.get(i);
					if (a.clicked) //牌被点击
						myCardPuts_List.add(a);
				}
				//存入临时list
				temp.addAll(myCardPuts_List);
				int count = 0;
				//判断点击出牌的是不是庄主，3个人都以庄主第一次发牌为准
				//1111111111111判断第一轮庄主
				System.out.println("~~~~~第一轮开始");
//				System.out.println("是否庄主:"+(whoBoss==LocalNumber));
				System.out.println("牌型:"+CardFunction.judgeHolder(myCardPuts_List));
				System.out.println("size:"+winnerPutFirstCardList.size());
				if(f % 4 == 1 &&CardFunction.judgeHolder(myCardPuts_List))
				{
					System.out.println("进入1");
					holderFlag = 0;//只判断1次(这里好像没用？）
					for (BeanCard a : myCardPuts_List) {
						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
						a.canClick = false;// 牌能否点击
						count++;
					}
					// 关闭出牌按钮
					bringOutCard_btn[0].setVisible(false);

					// 将出的牌整合为一个字符串
					String putCards = "";
					for (BeanCard c : myCardPuts_List) {
						putCards += c.getName() + " ";
					}
					//出牌消息type2发给服务器
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
					//发完牌，从持有牌清除发出的
					player[LocalNumber].cardList.removeAll(temp);
					//没牌，游戏结束信息发给服务器
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
				//222222222222222判断第一轮三个农民
				System.out.println("~~~~~第二轮开始");
//				System.out.println("庄主牌数："+holderPutFirstCardList.size());
				System.out.println("size:"+winnerPutFirstCardList.size());
//				System.out.println("是否农民："+(whoBoss!=LocalNumber));
//				System.out.println("~~~~~civilianFlag2:>=3?:"+(civilianFlag2<3));
////				System.out.println("civil："+(civilianFlag == 1));
				System.out.println("牌型:"+CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList));
				if(f % 4 != 1 &&CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList))
				{
					System.out.println("进入2");
					for (BeanCard a : myCardPuts_List) 
					{
						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
						a.canClick = false;// 牌能否点击
						count++;
					}
				 
					// 关闭出牌按钮
					bringOutCard_btn[0].setVisible(false);

					// 将出的牌整合为一个字符串
					String putCards = "";
					for (BeanCard c : myCardPuts_List) {
						putCards += c.getName() + " ";
					}
					//出牌消息type2发给服务器
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
					//发完牌，从持有牌清除发出的
					player[LocalNumber].cardList.removeAll(temp);
					//没牌，游戏结束信息发给服务器
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
//				System.out.println("~~~~~第三轮开始");
//				System.out.println("赢家牌数："+winnerPutFirstCardList.size());
//				System.out.println("~~~~~civilianFlag2:>=3?:"+(civilianFlag2<3));
//				System.out.println("牌型："+CardFunction.judgeHolder(myCardPuts_List));
//				System.out.println("whoWon是不是本地："+(staticWhoWon == LocalNumber));
//				System.out.println("whoWon值："+staticWhoWon);
//				System.out.println("holderFlag =="+holderFlag );
//				//3333333333333333判断赢家情况(和庄家的判断函数一致）
//				if(holderFlag == 0 &&civilianFlag2 >= 3&& staticWhoWon == LocalNumber && CardFunction.judgeHolder(myCardPuts_List))
//				{
//					
//					System.out.println("进入3");
//					for (BeanCard a : myCardPuts_List) {
//						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
//						a.canClick = false;// 牌能否点击
//						count++;
//					}
//					// 关闭出牌按钮
//					bringOutCard_btn[0].setVisible(false);
//
//					// 将出的牌整合为一个字符串
//					String putCards = "";
//					for (BeanCard c : myCardPuts_List) {
//						putCards += c.getName() + " ";
//					}
//					//出牌消息type2发给服务器
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
//					//发完牌，从持有牌清除发出的
//					player[LocalNumber].cardList.removeAll(temp);
//					//没牌，游戏结束信息发给服务器
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
//				System.out.println("~~~~~第四轮开始\n");
//				System.out.println("赢家牌数："+winnerPutFirstCardList.size());
//				System.out.println("牌型："+CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList));
//				System.out.println("whoWon不是本地："+(staticWhoWon != LocalNumber));
//				System.out.println("whoWon值："+staticWhoWon);
//				//非赢家 ,此时和赢家牌比较
//				if(civilianFlag2 >= 3 && holderFlag == 0 && staticWhoWon != LocalNumber&&CardFunction.cardCompare(myCardPuts_List, winnerPutFirstCardList, player[LocalNumber].cardList))
//				{
//					System.out.println("进入4");
//					for (BeanCard a : myCardPuts_List) {
//						CardFunction.move(a, getLocation(), new Point(400 + count * 15, 275));
//						a.canClick = false;// 牌能否点击
//						count++;
//					}
//					// 关闭出牌按钮
//					bringOutCard_btn[0].setVisible(false);
//
//					// 将出的牌整合为一个字符串
//					String putCards = "";
//					for (BeanCard c : myCardPuts_List) {
//						putCards += c.getName() + " ";
//					}
//					//出牌消息type2发给服务器
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
//					//发完牌，从持有牌清除发出的
//					player[LocalNumber].cardList.removeAll(temp);
//					//没牌，游戏结束信息发给服务器
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
//					//上家牌消失
//					for (Card a : myCardPuts_List) {
//						CardCtrl.move(a, getLocation(), new Point(400 + count * 15, 275));
//						a.canClick = false;// 牌能否点击
//						count++;
//					}
//
//					// 关闭出牌按钮
//					bringOutCard_btn[0].setVisible(false);
//
//					// 将出的牌整合为一个字符串
//					String putCards = "";
//					for (Card c : myCardPuts_List) {
//						putCards += c.getName() + " ";
//					}
//					//出牌消息type2发给服务器
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
//					//发完牌，从持有牌清除发出的
//					player[LocalNumber].cardList.removeAll(temp);
//					//没牌，游戏结束信息发给服务器
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
				// 出牌后重新排序
				reCardList();
				//把点起的牌降下
				for (int i = 0; i < player[LocalNumber].cardList.size(); i++) 
					player[LocalNumber].cardList.get(i).clicked = false;
			}
		});
	}

	// 出牌后重新排序
	protected void reCardList() 
	{
		for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) 
		{
			BeanCard a = player[LocalNumber].cardList.get(i);
			this.add(a);
			a.setBounds(300 + i * 15, 540, 71, 96);
		}
	}
	// 更新本地玩家序号
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
		// 设置東边卡牌位置
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
		// 设置北边卡牌位置
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
		// 更新除去本地玩家以外所有玩家的手牌
		upWest();
		upEast();
		upNorth();
	}

	// 获得各个方位玩家的号码
	public int getEastNum() {
		return (LocalNumber + 1) > 4 ? (LocalNumber + 1 - 4) : (LocalNumber + 1);
	}

	public int getWestNum() {
		return (LocalNumber + 3) > 4 ? (LocalNumber + 3 - 4) : (LocalNumber + 3);
	}

	public int getNorthNum() {
		return (LocalNumber + 2) > 4 ? (LocalNumber + 2 - 4) : (LocalNumber + 2);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~客户端线程~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public void run() {
		try {
			DataInputStream fromServer = new DataInputStream(socket.getInputStream());
			DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
			toServer.write(playerName.getBytes());//这里设置随机数名字传送二进制数据
			while (true) 
			{
				repaint();
				byte[] b = new byte[3000];
				fromServer.read(b);
				String msg = new String(b).trim();
				//System.out.println("服务端发来：" + msg);
				//牌
				JSONObject json = new JSONObject(msg);
				int commandType = json.getInt("type");
				//type：				pnum：玩家序号		msg：牌型+牌点
//				1.确定庄主
//				2.出牌消息
//				3.发牌消息
//				4.游戏结束
//				5.玩家序号/名称
//				6.获得8张扣牌
//				7.询问是否叫庄命令
//				8.计分+确定获胜者
				switch (commandType) 
				{
				//确定每一轮的获胜者，并加分
				//sendRoundWin();
				case 8:
					int winPlayer = json.getInt("pnum");
					int getScore = json.getInt("msg");
					whoWon = winPlayer;//标记获胜者
					//
					//operatingNum = winPlayer;
					player[winPlayer].setScore(player[winPlayer].getScore()+getScore);
					playerScore[0].setText(Integer.toString(player[getEastNum()].getScore()));
					playerScore[1].setText(Integer.toString(player[LocalNumber].getScore()));
					playerScore[2].setText(Integer.toString(player[getNorthNum()].getScore()));
					playerScore[3].setText(Integer.toString(player[getWestNum()].getScore()));
					break;
				case 1:
					// 收到确定庄主玩家 end
					for (int i = dizhuJLabels.length - 1; i >= 0; i--) 
					{
						dizhuJLabels[i].setVisible(false);
					}
					player = PlayerFunction.determineBoss(json, player, LocalNumber);
					//庄主的序号
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
					// 收到出牌
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
//						//确认第一轮庄主
//						if(holderFlag == 1&&num == whoBoss)
//						{
//							holderPutFirstCardList.addAll(lastCardPuts_List);
//							holderFlag = 0;//不再判断庄主
//							civilianFlag = 1;//开始判断第一轮三个玩家
//						}
//						//确认第一轮其他三个玩家
//						if(civilianFlag2 < 3&&civilianFlag == 1 && num!=whoBoss)
//						{
//							civilianFlag2++;//超过3次关闭判断（进入赢家环节）
//							if(whoWon>0)
//							{
//								staticWhoWon = whoWon;//最后一次要确认赢家。
//								f = 1;
//							}
//						}
//						if(num == staticWhoWon)
//						{
//							f =0;
//						}
//						//确认赢家第一次发牌(这个不能关闭，一直循环判断）
//						if(f!=1&&civilianFlag2 >= 3&&num == staticWhoWon)//whoWon已经被清零了，得用static
//						{
//							winnerPutFirstCardList.clear();
//							winnerPutFirstCardList.addAll(lastCardPuts_List);//
//							staticWhoWon = 0;
//						}
//						
//						if(civilianFlag2 >= 3&&whoWon > 0)//whoWon已经被清零了，得用static
//						{
//							staticWhoWon = whoWon;//这里也要一直判断；
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
					//顺时针判断
					num = num + 1;
					if (num > 4)
						num -= 4;//如果第4个玩家结束，应该从第1个开始，所以5-4；
					System.out.println("whoWOn:"+whoWon);
//这里做一个判断：每一轮还没有获胜者的时候，顺时针循环；有获胜者的时候，获胜者有出牌权，从获胜者开始顺时针出牌
					if(whoWon == 0)//没获胜者时（4个人没打完），顺时针显示
					{
						if (num == LocalNumber) 
							bringOutCard_btn[0].setVisible(true);
					}
					else 
						if(whoWon == LocalNumber)//有获胜者时，从
							bringOutCard_btn[0].setVisible(true);
					whoWon = 0;	//一回合结束要清零，等待case8
					break;
				case 3:// 收到发牌 end
					player = PlayerFunction.releaseCards(json, player, LocalNumber);
					for (int i = player[LocalNumber].cardList.size() - 1; i >= 0; i--) 
					{
						BeanCard a = player[LocalNumber].cardList.get(i);
						// a.canClick = true;
						this.add(a);
						a.setLocation(300 + i * 15, 540);
					}
					break;
				case 4:// 收到游戏结束
					int VictoryNum = json.getInt("pnum");
					String VictoryCamp = player[VictoryNum].isBoss ? "地主" : "农民";
					GameOver.show(player[VictoryNum].name + "出完了~\n" + VictoryCamp + "获得胜利~");
					this.setVisible(false);
					break;

				case 5:// 收到玩家序号及名称 end
					player = PlayerFunction.getLocalPlayer(json, player);
					getLocalPlayer(player);// 更新本地玩家序号-LocalNumber

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
				case 6:// 收到庄主牌 end
					bossCards = PlayerFunction.getBOssCards(json, bossCards);
					break;
				case 7:// 收到询问庄主命令 end
					whoBoss = json.getInt("pnum");
					//operatingNum = whoBoss;
					init();// 初始化主界面，设置长宽高，背景颜色等
					setWest();// 设置东边玩家的头像，姓名以及卡牌位置（需要动态获取玩家姓名与头像）
					setNorth();// 设置北边玩家的头像，姓名以及卡牌位置（需要动态获取玩家姓名与头像）
					setEast();// 设置南边玩家的头像，姓名以及卡牌位置（需要动态获取玩家姓名与头像）
					setCenter();// 开局设置中间庄主牌显示
					setSouth();// 设置南面（本地）玩家的姓名与头像，以及显示本地玩家的手牌
					setLocal();// 设置南面（本地）玩家的按钮的位置，默认为不可见。
					if (whoBoss == LocalNumber) {
						for (int i = 0; i < 2; i++) {
							scrambleLord_btn[i].setVisible(true);
						}
						selectPattern_Jbox.setVisible(true);
					}
					break;
				default:
					System.out.println("json数据不合法");
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
