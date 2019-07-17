package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Client.BeanCard;

public class SocketThreadFromClient implements Runnable {

	private DataInputStream fromClient;
	private ServerFunctionToClient sc;
	private int bossflag ;
	private Won_Count count = new Won_Count();

	public SocketThreadFromClient(ServerFunctionToClient sc, DataInputStream fromClient) {
		super();
		this.sc = sc;
		this.fromClient = fromClient;
		this.bossflag=0;
	}

	@Override
	public void run() 
	{
		try {
			while (true) 
			{
				byte [] b = new byte[10240];
				this.fromClient.read(b);
				String clientMsg = new String(b).trim();// 来自客户端的命令
				JSONObject json = new JSONObject(clientMsg);// 转化为json数据格式
				//System.out.println("服务器收到:"+clientMsg);
				switch (json.getInt("type")) {
				case 1:
					receiveSetBoss(json);
					break;
				case 2:
					//顺便计下分,返回获胜者编号以及得分
					int r[];
					r = count.wonWithCount(json);
					if(r!=null)
						sc.sendRoundWin(r[0], r[1]);
					receivePutCards(json);
					break;
				case 3:
					receiveGameOver(json);
					break;
				default:
					System.out.println("json:error!");
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//收到叫庄
	private void receiveSetBoss(JSONObject json) throws JSONException, IOException {
		int num = json.getInt("pnum");
		String flag = json.getString("msg");
		if(flag.equals("yes")){
			sc.sendBoss(num);
			sc.sendBossCards(num);
		}else if(flag.equals("no")){
			bossflag++;
			if(bossflag==4){
				sc.sendGameOver(0);
			}else{
				sc.sendBossMsg((num+1)>4?(num-3):(num+1));
			}
		}
	}
	//收到出牌命令
	private void receivePutCards(JSONObject json) throws JSONException, IOException {
		int num = json.getInt("pnum");
		String cards = json.getString("msg");
		sc.sendPutCards(num, cards);
	}
	
	//收到胜利命令
	private void receiveGameOver(JSONObject json) throws JSONException, IOException {
		int num = json.getInt("pnum");
		sc.sendGameOver(num);
	}
}
