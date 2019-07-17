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
			//ȡ�ø���ҷ�����(����1~2��
			String putCardsList[] = json.getString("msg").split(" ");
			//���������Ϊ��һ����ҵĵ�һ����
			if(index++ == 1)
			{
				maxPointsCard = new BeanCard(putCardsList[0],false);
				maxPointsPlayer = json.getInt("pnum");
			}
			//�������ҳ�5��10��K
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
			System.out.println("~~~~~~~~~~~~~~�� "+turnCount+" ��~~~~~~~~~");
			//���÷���˷��������ӷ���Ϣ���ͻ���
			if(turnCount == 4)
			{
				System.out.println("~~~~~~��ʤ�ߣ�"+maxPointsPlayer+"~~~~~�÷֣�"+roundAllScore);
				int r[] = {maxPointsPlayer,roundAllScore};
				//���¿�ʼ��һ�ֻ���
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
	
	//ֻ�����ִ�С����ɫ���ñ�
	public int compare(BeanCard card1, BeanCard card2) 
	{
		int a1 = card1.getPattern();// a��ɫ
		int a2 = card1.getPoints();// a��ֵ
		int b1 = card2.getPattern();// b��ɫ
		int b2 = card2.getPoints();// b��ֵ
		if (a1 == 2 && a2 == 5)
			a2 += 200;
		if (b1 == 2 && b2 == 5)
			b2 += 200;
		// ��������Ļ�
		if (a1 == 5)
			a2 += 100;
		if (a1 == 5 && a2 == 1)
			a2 += 50;
		if (b1 == 5)
			b2 += 100;
		if (b1 == 5 && b2 == 1)
			b2 += 50;
		// �����A����2
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
