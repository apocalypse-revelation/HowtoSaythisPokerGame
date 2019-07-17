package Client;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CardFunction {

	//���������ж�
	public static boolean eligibleCast(List<BeanCard> myputs)
	{
		if(myputs.size() == 8)
			return true;
		return false;
	}
	//��һ�غ�ׯ�����ͱȽ�
	public static boolean judgeHolder(List<BeanCard> a) 
	{
		if(a.size() == 1)
			return true;
		if(a.size() == 2 && a.get(0).getPoints() == a.get(1).getPoints()&& a.get(0).getPattern() == a.get(1).getPattern())
			return true;
		return false;
	}
	//3��������ͱȽ�
	public static boolean cardCompare(List<BeanCard> a,List<BeanCard> holder,List<BeanCard> allCard) 
	{
		if(a.size() != holder.size())//�������
			return false;
		int winnerPattern = holder.get(0).getPattern();
		//��ɫ��һ�£�(ֻ�Ƚϵ�һ�ţ�
		//1����
		if(a.size() == 1)
		{
			int myPattern = a.get(0).getPattern();
			int myPoints = a.get(0).getPoints();
			if(myPattern == 5 || (myPattern == 2|| myPoints == 5))//��С�����ߺ�����
				return true;
			if(winnerPattern == myPattern)
				return true;
			else if(winnerPattern != myPattern)
			{
				for(int i = 0; i < allCard.size(); i++)
				{
					if(allCard.get(i).getPattern() == winnerPattern)
						return false;
				}
				return true;
			}
		}
		//2����
		else if(a.size() == 2)
		{
			
			int myPattern1 = a.get(0).getPattern();
			int myPattern2 = a.get(1).getPattern();
			int myPoints1 = a.get(0).getPoints();
			int myPoints2 = a.get(1).getPoints();
			int flag1 = 0;
			if(myPattern1 == 5 || (myPattern1 == 2|| myPoints1 == 5))//��С�����ߺ�����
				return true;
			if(myPattern2 == 5 || (myPattern2 == 2|| myPoints2 == 5))
				return true;
			if(winnerPattern == myPattern1&&winnerPattern == myPattern2)
				return true;
			//2��������һ�Ų�����Ӯ�ҵĻ�ɫ
			if((winnerPattern != myPattern1&&winnerPattern == myPattern2)|| (winnerPattern != myPattern2&&winnerPattern == myPattern1))
			{
				for(int i = 0; i < allCard.size(); i++)
				{
					if(allCard.get(i).getPattern() == winnerPattern)
					{
						flag1++;
					}
				}
				if(flag1 == 1)
					return true;
				return false;
			}
			//2���ƶ�������Ӯ�ҵĻ�ɫ
			else if(winnerPattern != myPattern1 && winnerPattern != myPattern2)
			{
				for(int i = 0; i < allCard.size(); i++)
				{
					if(allCard.get(i).getPattern() == winnerPattern)
						return false;
				}
				return true;
			}
		}
		//����2����
		else if(a.size() > 2)
			return false;
		return false;
	}
	//̫���˹�������
//	public static boolean firstRoundCompare(List<Card> a,List<Card> holder,List<Card> allCard) 
//	{
//		if(a.size() != holder.size())//�������
//			return false;
//		//1�������
//		if(a.size() == 1&&holder.size() == 1)
//		{
//			if(a.get(0).getPattern() == holder.get(0).getPattern())
//				return true;
//			else if(a.get(0).getPattern() != holder.get(0).getPattern())
//			{
//				for(int i = 0; i < allCard.size(); i++)
//				{
//					if(allCard.get(i).getPattern() == holder.get(0).getPattern())
//						return false;
//				}
//				return true;
//			}
//		}
//		//2���������//�жϻ�ɫ�Ƿ�һ��:�ȿ��Ƿ��ܳ����ӣ����ȷʵû�Ʋ��ܳ�˫��
//		else if(holder.size() == 2)	
//		{
//			//һ�Ż�ɫ������
//			if(a.get(0).getPattern() == holder.get(0).getPattern())
//			{
//				if(a.get(1).getPattern() == holder.get(1).getPattern())
//					return true;//2��2��ɫһ��
//				//�ڶ��Ų����ڵ������û�Ʋſ���true
//				else if(a.get(1).getPattern() != holder.get(1).getPattern())
//				{
//					for(int i = 0; i < allCard.size(); i++)
//					{
//						if(allCard.get(i).getPattern() == holder.get(1).getPattern())
//							if(allCard.get(i).getPoints() == holder.get(1).getPoints())
//								return false;
//					}
//					return true;//�Ҳ����ͷ���true
//				}
//			}
//			//���Ż�ɫ��������
//			if(a.get(0).getPattern() != holder.get(0).getPattern())
//			{
//				if(a.get(1).getPattern() != holder.get(1).getPattern())
//					for(int i = 0; i < allCard.size(); i++)
//					{
//						//�������ж��Ƿ���һ�Ż�ɫ��һ����
//						if(allCard.get(i).getPattern() == holder.get(0).getPattern())
//							return false;
//					}
//				return true;
//			}
//		}
//		return false;
//	}
	// �������ͱȽ�,a����b����true
//	public static boolean cardListCompare(List<Card> a, List<Card> b, List<Card> myCardList) 
//	{
//		int flag = 0;
//		//���ж��Ƿ�ׯ����Ӧ����8��
//		if(myCardList.size()>25&&a.size()!=8&&b.size()==0)
//			return false;
//		//���жϲ��ܳ�2�����ϵ���
//		if(a.size()>2 || b.size()>2)
//			return false;
//		//���ж��ǲ��ǵ�һ�غϳ��ƣ���ʱ��bΪ0�����ұ��Ҳ��ܳ�˫��ֻ�ܳ���/���ӣ�
//		if(a.size() == 1&&b.size() == 0)
//			return true;
//		if(a.get(0).getPattern()!=a.get(1).getPattern()&&b.size()==0)
//			return false;
//		else if(a.get(0).getPattern()==a.get(1).getPattern()&&b.size()==0)
//			return true;
//		//���жϽ��Ƶ������Ƿ����
//		if(a.size()!=b.size())
//			return false;
//		//����жϻ�ɫ�Ƿ�һ��:�ȿ��Ƿ��ܳ����ӣ����ȷʵû�Ʋ��ܳ�˫
//		int pattern = b.get(0).getPattern();
//		for(int i = 0; i < myCardList.size(); i++)
//			//��ɫ����ֵ��һ����2�ξͲ��ܳ�
//			//���ҳ��������л�ɫһ����
//			if(b.get(0).getPattern() == myCardList.get(i).getPattern())
//				//�Ըû�ɫһ�����ƣ��ٴӳ����Ʊȶ�һ��(����ֵ��ͬ�ģ�
//				//���ǵ������������ͷ���бȶ�
//			{
//				if(flag == 2) return false;
//				else if(flag < 2) return true;
//				flag = 0;
//				for(int j = 0; j < myCardList.size(); j++)
//					if(myCardList.get(i).getPoints()==myCardList.get(j).getPoints())
//						flag++;
//			}
//		return false;
//	}
	// ���ƱȽ�
	public static boolean cardCompare(BeanCard a, BeanCard b) 
	{
		int a1 = a.getPattern();// a��ɫ
		int a2 = a.getPoints();// a��ֵ
		int b1 = b.getPattern();// b��ɫ
		int b2 = b.getPoints();// b��ֵ
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
		return a2 - b2 > 0 ? true : false;
	}

	// ûѡ��ɫ�Ŀ���List����
	public static void cardListSort(List<BeanCard> list) 
	{
		Collections.sort(list, new Comparator<BeanCard>() 
		{
			@Override
			public int compare(BeanCard o1, BeanCard o2) 
			{
				// TODO Auto-generated method stub
				int a1 = o1.getPattern();// a��ɫ
				int a2 = o1.getPoints();// a��ֵ
				int b1 = o2.getPattern(); //b
				int b2 = o2.getPoints();//b
				int flag = 0;//�����Ʊȴ�С�ķ���ֵ
				//����5
				if(a1 == 2 && a2 == 5)
					a2 += 200;
				//��
				if (a1 == 5)
					a2 += 100;
				//С��
				if (a1 == 5 && a2 == 1)
					a2 += 50;
				//����5
				if(b1 == 2 && b2 == 5)
					b2 += 200;
				//��
				if (b1 == 5)
					b2 += 100;
				//С��
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
				//�Ƚ�����
				flag = b2 - a2;
				if (flag == 0)
					return b1 - a1;
				else {
					return flag;
				}
			}
		});
	}
	//ѡ�˻�ɫ�������
	public static void cardListSort(List<BeanCard> list,int pattern) 
	{
		Collections.sort(list, new Comparator<BeanCard>() 
		{
			@Override
			public int compare(BeanCard o1, BeanCard o2) 
			{
				// TODO Auto-generated method stub
				int a1 = o1.getPattern();// a��ɫ
				int a2 = o1.getPoints();// a��ֵ
				int b1 = o2.getPattern(); //b
				int b2 = o2.getPoints();//b
				int flag = 0;//�����Ʊȴ�С�ķ���ֵ
				//������Ϊ��ɫ���ı��С����
				//����5
				if(a1 == 2 && a2 == 5)
					a2 += 500;
				//��
				if (a1 == 5)
					a2 += 100;
				//С��
				if (a1 == 5 && a2 == 1)
					a2 += 50;
				//����5
				if(b1 == 2 && b2 == 5)
					b2 += 500;
				//��
				if (b1 == 5)
					b2 += 100;
				//С��
				if (b1 == 5 && b2 == 1)
					b2 += 50;
				// �����A
				if (a2 == 1)
					a2 += 20;
				if (b2 == 1)
					b2 += 20;
//				if (a2 == 2)
//					a2 += 30;
//				if (b2 == 2)
//					b2 += 30;
				//������ı
				if(pattern == 1)
				{
					if(a1 == 1&&a2 == 3)// ����ı
						a2 += 40;
					if(a1 == 3&&a2 == 3)// ����ı
						a2 += 35;
					if(b1 == 1&&b2 == 3)// ����ı
						b2 += 40;
					if(b1 == 3&&b2 == 3)// ����ı
						b2 += 35;
				}
				else if(pattern == 2)
				{
					if(a1 == 2&&a2 == 3)// ����ı
						a2 += 40;
					if(a1 == 4&&a2 == 3)// ����ı
						a2 += 35;
					if(b1 == 2&&b2 == 3)// ����ı
						b2 += 40;
					if(b1 == 4&&b2 == 3)// ����ı
						b2 += 35;
				}
				else if(pattern == 3)
				{
					if(a1 == 3&&a2 == 3)// ����ı
						a2 += 40;
					if(a1 == 1&&a2 == 3)// ����ı
						a2 += 35;
					if(b1 == 3&&b2 == 3)// ����ı
						b2 += 40;
					if(b1 == 1&&b2 == 3)// ����ı
						b2 += 35;
				}
				else if(pattern == 4)
				{
					if(a1 == 4&&a2 == 3)// ����ı
						a2 += 40;
					if(a1 == 2&&a2 == 3)// ����ı
						a2 += 35;
					if(b1 == 4&&b2 == 3)// ����ı
						b2 += 40;
					if(b1 == 2&&b2 == 3)// ����ı
						b2 += 35;
				}
				//��������2
				if(pattern == 1)
				{
					if(a1 == 1 && a2 == 2)// ��2
						a2 += 30;
					else if(a2 == 2)		// ��2
						a2 += 25;
					if(b1 == 1 && b2 == 2)// ��2
						b2 += 30;
					else if(b2 == 2)		// ��2
						b2 += 25;
				}
				else if(pattern == 2)
				{
					if(a1 == 2 && a2 == 2)// ��2
						a2 += 30;
					else if(a2 == 2)		// ��2
						a2 += 25;
					if(b1 == 2 && b2 == 2)// ��2
						b2 += 30;
					else if(b2 == 2)		// ��2
						b2 += 25;
				}
				else if(pattern == 3)
				{
					if(a1 == 3 && a2 == 2)// ��2
						a2 += 30;
					else if(a2 == 2)		// ��2
						a2 += 25;
					if(b1 == 3 && b2 == 2)// ��2
						b2 += 30;
					else if(b2 == 2)		// ��2
						b2 += 25;
				}
				else if(pattern == 4)
				{
					if(a1 == 4 && a2 == 2)// ��2
						a2 += 30;
					else if(a2 == 2)		// ��2
						a2 += 25;
					if(b1 == 4 && b2 == 2)// ��2
						b2 += 30;
					else if(b2 == 2)		// ��2
						b2 += 25;
				}
				//����ɻ�ɫ���������ƵĴ�С
				if(a1 == pattern)
					//Ĭ�ϼ�1����������ɫ����
					a2 += 1;
				
				//�Ƚ�����
				flag = b2 - a2;
				if (flag == 0)
					return b1 - a1;
				else {
					return flag;
				}
			}
		});
	}

	// �����ƶ�����
	public static void move(BeanCard card, Point from, Point to) 
	{
		if(to.x!=from.x)
		{
			double k=(1.0)*(to.y-from.y)/(to.x-from.x);
			double b=to.y-to.x*k;
			int flag=0;//�ж������������ƶ�����
			if(from.x<to.x)
				flag=20;
			else 
			{
				flag=-20;
			}
			for(int i=from.x;Math.abs(i-to.x)>20;i+=flag)
			{
				double y=k*i+b;//������Ҫ�õ���ѧ�е����Ժ���

				card.setLocation(i,(int)y);
				try {
					Thread.sleep(5); //�ӳ٣����Լ�����
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
