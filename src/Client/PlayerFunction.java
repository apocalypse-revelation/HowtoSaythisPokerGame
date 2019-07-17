package Client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerFunction {
	// �յ�ׯ����
	public static List<BeanCard> getBOssCards(JSONObject json, List<BeanCard> bossCards) throws JSONException 
	{
		String s = json.getString("msg");
		String[] ss = s.split(" ");
		for (int i = 0; i < ss.length; i++) {
			BeanCard a = new BeanCard(ss[i], true);
			a.canClick = true;
			bossCards.add(a);
		}
		return bossCards;
	}

	// �յ�ȷ��ׯ�����
	public static BeanPlayer[] determineBoss(JSONObject json, BeanPlayer[] playerList, int LocalNumber) throws JSONException 
	{
		int n = json.getInt("pnum");
		playerList[n].isBoss = true;
		if (n != LocalNumber)
			for (int i = 1; i <= 4; i++) 
			{
				if (i != n)
					continue;
				BeanCard a = new BeanCard("1-1", false);
				a.canClick = false;
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
			}
		return playerList;
	}

	// �յ�����
	public static List<BeanCard> takeCards(JSONObject json) throws JSONException 
	{
		String s = json.getString("msg");
		List<BeanCard> List = new ArrayList<BeanCard>();
		String[] ss = s.split(" ");
		for (int i = 0; i < ss.length; i++) 
		{
			BeanCard a = new BeanCard(ss[i], true);
			a.canClick = false;
			List.add(a);
		}
		return List;
	}

	// �յ�����
	public static BeanPlayer[] releaseCards(JSONObject json, BeanPlayer[] playerList, int num) throws JSONException 
	{
		String s = json.getString("msg");
		String[] ss = s.split(" ");
		for (int i = 0; i < ss.length; i++) {
			BeanCard a = new BeanCard(ss[i], true);
			a.canClick = true;
			playerList[num].cardList.add(a);
		}
		return playerList;
	}

	// �յ������ż�����
	public static BeanPlayer[] getLocalPlayer(JSONObject json, BeanPlayer[] playerList) throws JSONException 
	{
		String str = json.getString("msg");
		String[] s = str.split(" ");
		for (int i = 1; i <= 4; i++) {
			playerList[i].name = s[i - 1];
			playerList[i].playNumber = i;
		}
		return playerList;
	}
}
