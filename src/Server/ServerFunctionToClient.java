package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Client.BeanCard;
import Client.CardFunction;

public class ServerFunctionToClient {

	Socket[] socket = new Socket[5];
	DataOutputStream[] toClient = new DataOutputStream[5];
	DataInputStream[] fromClient = new DataInputStream[5];
	List<BeanCard> bossList;

	public ServerFunctionToClient(Socket[] socket, DataOutputStream[] toClient, DataInputStream[] fromClient) {
		super();
		this.socket = socket;
		this.toClient = toClient;
		this.fromClient = fromClient;
	}

	// 计分
	// type8：发送加分消息；num：给谁加分；getpoints：加分多少
		public void sendRoundWin(int num,int getPoints) throws JSONException, IOException 
		{
			JSONObject json = new JSONObject();
			json.put("type", 8);
			json.put("pnum", num);
			json.put("msg", getPoints);
//			JSONObject json1 = new JSONObject();
//			json1.put("type", 2);
//			json1.put("mark", num);
//			json1.put("msg", "10000");
			sendMsgToClient(json);
//			sendMsgToClient(json1);
		}
	// 确定叫庄 type1
	public void sendBoss(int num) throws JSONException, IOException 
	{
		JSONObject json = new JSONObject();
		json.put("type", 1);
		json.put("pnum", num);
		sendMsgToClient(json);
	}

	// 询问叫庄 type7
	public void sendBossMsg(int num) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("type", 7);
		json.put("pnum", num);
		sendMsgToClient(json);
	}

	// 出牌 type2
	public void sendPutCards(int num, String cards) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("type", 2);
		json.put("pnum", num);
		json.put("msg", cards);
		sendMsgToClient(json);
	}

	// 发牌 type3
	public void sendCards() throws JSONException, IOException 
	{
		List<BeanCard> list = new ArrayList<>(); 
		List<BeanCard> player1 = new ArrayList<>(); 
		List<BeanCard> player2 = new ArrayList<>(); 
		List<BeanCard> player3 = new ArrayList<>(); 
		List<BeanCard> player4 = new ArrayList<>(); 
		this.bossList = new ArrayList<>(); // 定义地主牌
		//初始化基本牌型(i=4种花色，j=13种点数)
		for (int i = 1; i <= 4; i++) 
		{
			for (int j = 1; j <= 13; j++) 
			{
				String c = Integer.toString(i);
				c += "-";
				c += Integer.toString(j);
				list.add(new BeanCard(c, false));
				list.add(new BeanCard(c, false));
			}
		}
		//初始化大王小王（各2张）
		list.add(new BeanCard("5-1", false));
		list.add(new BeanCard("5-1", false));
		list.add(new BeanCard("5-2", false));
		list.add(new BeanCard("5-2", false));
		//洗牌
		Collections.shuffle(list);
		//开始发牌
		for (int j = 0; j < 108;) 
		{
			player1.add(list.get(j++));
			player2.add(list.get(j++));
			player3.add(list.get(j++));
			player4.add(list.get(j++));
			//8张扣牌
			if (bossList.size() < 8)
				bossList.add(list.get(j++));
		}
		//给牌排序
		CardFunction.cardListSort(player1);
		CardFunction.cardListSort(player2);
		CardFunction.cardListSort(player3);
		CardFunction.cardListSort(player4);

		String cards1 = "";
		String cards2 = "";
		String cards3 = "";
		String cards4 = "";
		for (int i = 0; i < player1.size(); i++) {
			cards1 += player1.get(i).name + " ";
			cards2 += player2.get(i).name + " ";
			cards3 += player3.get(i).name + " ";
			cards4 += player4.get(i).name + " ";
		}
		JSONObject p1 = new JSONObject();
		JSONObject p2 = new JSONObject();
		JSONObject p3 = new JSONObject();
		JSONObject p4 = new JSONObject();
		//发牌
		p1.put("type", 3);
		p1.put("pnum", 1);
		p1.put("msg", cards1.trim());

		p2.put("type", 3);
		p2.put("pnum", 2);
		p2.put("msg", cards2.trim());

		p3.put("type", 3);
		p3.put("pnum", 3);
		p3.put("msg", cards3.trim());

		p4.put("type", 3);
		p4.put("pnum", 4);
		p4.put("msg", cards4.trim());

		toClient[1].write(p1.toString().getBytes());
		toClient[2].write(p2.toString().getBytes());
		toClient[3].write(p3.toString().getBytes());
		toClient[4].write(p4.toString().getBytes());
	}

	// 发底牌
	public void sendBossCards(int num) throws JSONException, IOException 
	{
		String bossString = "";
		for (int i = 0; i < bossList.size(); i++) 
		{
			bossString += bossList.get(i).getName() + " ";
		}

		JSONObject json = new JSONObject();
		json.put("type", 6);
		json.put("pnum", num);
		json.put("msg", bossString);
		toClient[num].write(json.toString().getBytes());
	}

	// 玩家信息
	public void setPlayer(String playername) throws JSONException, IOException 
	{
		JSONObject json = new JSONObject();
		json.put("type", 5);
		json.put("msg", playername);
		System.out.println(json.toString());
		sendMsgToClient(json);
	}

	// 游戏结束
	public void sendGameOver(int num) throws JSONException, IOException {
		if (num == 0) {
			sendCards();
		} else {
			JSONObject json = new JSONObject();
			json.put("type", 4);
			json.put("pnum", num);
			sendMsgToClient(json);
		}
	}

	// 发送给客户端
	private void sendMsgToClient(JSONObject json) throws IOException {
		for (int i = 1; i <= 4; i++) 
		{
			toClient[i].write(json.toString().getBytes());
		}
		//System.out.println("服务器发送"+json.toString());
	}
}
