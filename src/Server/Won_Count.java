package Server;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import Client.BeanCard;
import Server.ServerFunctionToClient;

public class Won_Count {
	
	private static int turnCount = 1;
	private static int roundAllScore = 0;
	private static int maxPointsPlayer = 0;
	private static BeanCard maxPointsCard = null;
	
	private static int index = 1;

	
	
	public int[] wonWithCount(JSONObject json)
	{
		try
		{
			//取得该玩家发的牌(牌数1~2）
			String putCardsList[] = json.getString("msg").split(" ");
			//假设最大牌为第一个玩家的第一张牌
			if(index++ == 1)
			{
				maxPointsCard = new BeanCard(putCardsList[0],false);
				maxPointsPlayer = json.getInt("pnum");
			}
			//从牌中找出5，10，K
			for(int i = 0; i < putCardsList.length; i++)
			{
				BeanCard card = new BeanCard(putCardsList[i],false);
				if(card.getPoints() == 5)
					roundAllScore += 5;
				if(card.getPoints() == 10||card.getPoints() == 13)
					roundAllScore += 10;
			}
			BeanCard card = new BeanCard(putCardsList[0],false);
			if(compare(card,maxPointsCard)>0)
			{
				maxPointsCard = card;
				maxPointsPlayer = json.getInt("pnum");
			}
			System.out.println("~~~~~~~~~~~~~~第 "+turnCount+" 次~~~~~~~~~");
			//调用服务端方法，发加分信息给客户端
			if(turnCount == 4)
			{
				System.out.println("~~~~~~获胜者："+maxPointsPlayer+"~~~~~得分："+roundAllScore);
				int r[] = {maxPointsPlayer,roundAllScore};
				//重新开始新一轮积分
				roundAllScore = 0;
				maxPointsPlayer = 0;
				index = 1;
				maxPointsCard = null;
				turnCount = 1;
				return r;
			}
			else
				turnCount++;
		}catch (JSONException e) {
			// TODO: handle exception
		}
		return null;
	}
	
	//只比数字大小，花色不用比
	public int compare(BeanCard card1, BeanCard card2) 
	{
		int a1 = card1.getPattern();// a花色
		int a2 = card1.getPoints();// a数值
		int b1 = card2.getPattern();// b花色
		int b2 = card2.getPoints();// b数值
		if (a1 == 2 && a2 == 5)
			a2 += 200;
		if (b1 == 2 && b2 == 5)
			b2 += 200;
		// 如果是王的话
		if (a1 == 5)
			a2 += 100;
		if (a1 == 5 && a2 == 1)
			a2 += 50;
		if (b1 == 5)
			b2 += 100;
		if (b1 == 5 && b2 == 1)
			b2 += 50;
		// 如果是A或者2
		if (a2 == 1)
			a2 += 20;
		if (b2 == 1)
			b2 += 20;
		if (a2 == 2)
			a2 += 30;
		if (b2 == 2)
			b2 += 30;
		return a2 - b2;
	}
}
