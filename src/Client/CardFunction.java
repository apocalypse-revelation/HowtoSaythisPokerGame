package Client;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CardFunction {

	//弃牌牌型判断
	public static boolean eligibleCast(List<BeanCard> myputs)
	{
		if(myputs.size() == 8)
			return true;
		return false;
	}
	//第一回合庄主牌型比较
	public static boolean judgeHolder(List<BeanCard> a) 
	{
		if(a.size() == 1)
			return true;
		if(a.size() == 2 && a.get(0).getPoints() == a.get(1).getPoints()&& a.get(0).getPattern() == a.get(1).getPattern())
			return true;
		return false;
	}
	//3个玩家牌型比较
	public static boolean cardCompare(List<BeanCard> a,List<BeanCard> holder,List<BeanCard> allCard) 
	{
		if(a.size() != holder.size())//牌数相等
			return false;
		int winnerPattern = holder.get(0).getPattern();
		//花色不一致？(只比较第一张）
		//1张牌
		if(a.size() == 1)
		{
			int myPattern = a.get(0).getPattern();
			int myPoints = a.get(0).getPoints();
			if(myPattern == 5 || (myPattern == 2|| myPoints == 5))//大小王或者红桃五
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
		//2张牌
		else if(a.size() == 2)
		{
			
			int myPattern1 = a.get(0).getPattern();
			int myPattern2 = a.get(1).getPattern();
			int myPoints1 = a.get(0).getPoints();
			int myPoints2 = a.get(1).getPoints();
			int flag1 = 0;
			if(myPattern1 == 5 || (myPattern1 == 2|| myPoints1 == 5))//大小王或者红桃五
				return true;
			if(myPattern2 == 5 || (myPattern2 == 2|| myPoints2 == 5))
				return true;
			if(winnerPattern == myPattern1&&winnerPattern == myPattern2)
				return true;
			//2张牌中有一张不等于赢家的花色
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
			//2张牌都不等于赢家的花色
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
		//大于2张牌
		else if(a.size() > 2)
			return false;
		return false;
	}
	//太难了规则，舍弃
//	public static boolean firstRoundCompare(List<Card> a,List<Card> holder,List<Card> allCard) 
//	{
//		if(a.size() != holder.size())//牌数相等
//			return false;
//		//1张牌情况
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
//		//2张牌情况（//判断花色是否一致:先看是否还能出对子，如果确实没牌才能出双）
//		else if(holder.size() == 2)	
//		{
//			//一张花色不等于
//			if(a.get(0).getPattern() == holder.get(0).getPattern())
//			{
//				if(a.get(1).getPattern() == holder.get(1).getPattern())
//					return true;//2对2花色一致
//				//第二张不等于的情况，没牌才可以true
//				else if(a.get(1).getPattern() != holder.get(1).getPattern())
//				{
//					for(int i = 0; i < allCard.size(); i++)
//					{
//						if(allCard.get(i).getPattern() == holder.get(1).getPattern())
//							if(allCard.get(i).getPoints() == holder.get(1).getPoints())
//								return false;
//					}
//					return true;//找不到就返回true
//				}
//			}
//			//两张花色都不等于
//			if(a.get(0).getPattern() != holder.get(0).getPattern())
//			{
//				if(a.get(1).getPattern() != holder.get(1).getPattern())
//					for(int i = 0; i < allCard.size(); i++)
//					{
//						//牌组里判断是否有一张花色是一样的
//						if(allCard.get(i).getPattern() == holder.get(0).getPattern())
//							return false;
//					}
//				return true;
//			}
//		}
//		return false;
//	}
	// 出牌牌型比较,a大于b返回true
//	public static boolean cardListCompare(List<Card> a, List<Card> b, List<Card> myCardList) 
//	{
//		int flag = 0;
//		//先判断是否庄主，应该弃8张
//		if(myCardList.size()>25&&a.size()!=8&&b.size()==0)
//			return false;
//		//再判断不能出2张以上的牌
//		if(a.size()>2 || b.size()>2)
//			return false;
//		//再判断是不是第一回合出牌（此时，b为0），且本家不能出双（只能出单/对子）
//		if(a.size() == 1&&b.size() == 0)
//			return true;
//		if(a.get(0).getPattern()!=a.get(1).getPattern()&&b.size()==0)
//			return false;
//		else if(a.get(0).getPattern()==a.get(1).getPattern()&&b.size()==0)
//			return true;
//		//再判断接牌的牌数是否相符
//		if(a.size()!=b.size())
//			return false;
//		//最后判断花色是否一致:先看是否还能出对子，如果确实没牌才能出双
//		int pattern = b.get(0).getPattern();
//		for(int i = 0; i < myCardList.size(); i++)
//			//花色和数值都一样，2次就不能出
//			//先找出持有牌中花色一样的
//			if(b.get(0).getPattern() == myCardList.get(i).getPattern())
//				//对该花色一样的牌，再从持有牌比对一次(比数值相同的）
//				//考虑到数组溢出，从头进行比对
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
	// 卡牌比较
	public static boolean cardCompare(BeanCard a, BeanCard b) 
	{
		int a1 = a.getPattern();// a花色
		int a2 = a.getPoints();// a数值
		int b1 = b.getPattern();// b花色
		int b2 = b.getPoints();// b数值
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
		return a2 - b2 > 0 ? true : false;
	}

	// 没选花色的卡牌List排序
	public static void cardListSort(List<BeanCard> list) 
	{
		Collections.sort(list, new Comparator<BeanCard>() 
		{
			@Override
			public int compare(BeanCard o1, BeanCard o2) 
			{
				// TODO Auto-generated method stub
				int a1 = o1.getPattern();// a花色
				int a2 = o1.getPoints();// a数值
				int b1 = o2.getPattern(); //b
				int b2 = o2.getPoints();//b
				int flag = 0;//两张牌比大小的返回值
				//红桃5
				if(a1 == 2 && a2 == 5)
					a2 += 200;
				//王
				if (a1 == 5)
					a2 += 100;
				//小王
				if (a1 == 5 && a2 == 1)
					a2 += 50;
				//红桃5
				if(b1 == 2 && b2 == 5)
					b2 += 200;
				//王
				if (b1 == 5)
					b2 += 100;
				//小王
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
				//比较牌型
				flag = b2 - a2;
				if (flag == 0)
					return b1 - a1;
				else {
					return flag;
				}
			}
		});
	}
	//选了花色后的排序
	public static void cardListSort(List<BeanCard> list,int pattern) 
	{
		Collections.sort(list, new Comparator<BeanCard>() 
		{
			@Override
			public int compare(BeanCard o1, BeanCard o2) 
			{
				// TODO Auto-generated method stub
				int a1 = o1.getPattern();// a花色
				int a2 = o1.getPoints();// a数值
				int b1 = o2.getPattern(); //b
				int b2 = o2.getPoints();//b
				int flag = 0;//两张牌比大小的返回值
				//不会因为花色而改变大小的牌
				//红桃5
				if(a1 == 2 && a2 == 5)
					a2 += 500;
				//王
				if (a1 == 5)
					a2 += 100;
				//小王
				if (a1 == 5 && a2 == 1)
					a2 += 50;
				//红桃5
				if(b1 == 2 && b2 == 5)
					b2 += 500;
				//王
				if (b1 == 5)
					b2 += 100;
				//小王
				if (b1 == 5 && b2 == 1)
					b2 += 50;
				// 如果是A
				if (a2 == 1)
					a2 += 20;
				if (b2 == 1)
					b2 += 20;
//				if (a2 == 2)
//					a2 += 30;
//				if (b2 == 2)
//					b2 += 30;
				//决定参谋
				if(pattern == 1)
				{
					if(a1 == 1&&a2 == 3)// 正参谋
						a2 += 40;
					if(a1 == 3&&a2 == 3)// 副参谋
						a2 += 35;
					if(b1 == 1&&b2 == 3)// 正参谋
						b2 += 40;
					if(b1 == 3&&b2 == 3)// 副参谋
						b2 += 35;
				}
				else if(pattern == 2)
				{
					if(a1 == 2&&a2 == 3)// 正参谋
						a2 += 40;
					if(a1 == 4&&a2 == 3)// 副参谋
						a2 += 35;
					if(b1 == 2&&b2 == 3)// 正参谋
						b2 += 40;
					if(b1 == 4&&b2 == 3)// 副参谋
						b2 += 35;
				}
				else if(pattern == 3)
				{
					if(a1 == 3&&a2 == 3)// 正参谋
						a2 += 40;
					if(a1 == 1&&a2 == 3)// 副参谋
						a2 += 35;
					if(b1 == 3&&b2 == 3)// 正参谋
						b2 += 40;
					if(b1 == 1&&b2 == 3)// 副参谋
						b2 += 35;
				}
				else if(pattern == 4)
				{
					if(a1 == 4&&a2 == 3)// 正参谋
						a2 += 40;
					if(a1 == 2&&a2 == 3)// 副参谋
						a2 += 35;
					if(b1 == 4&&b2 == 3)// 正参谋
						b2 += 40;
					if(b1 == 2&&b2 == 3)// 副参谋
						b2 += 35;
				}
				//决定主副2
				if(pattern == 1)
				{
					if(a1 == 1 && a2 == 2)// 主2
						a2 += 30;
					else if(a2 == 2)		// 副2
						a2 += 25;
					if(b1 == 1 && b2 == 2)// 主2
						b2 += 30;
					else if(b2 == 2)		// 副2
						b2 += 25;
				}
				else if(pattern == 2)
				{
					if(a1 == 2 && a2 == 2)// 主2
						a2 += 30;
					else if(a2 == 2)		// 副2
						a2 += 25;
					if(b1 == 2 && b2 == 2)// 主2
						b2 += 30;
					else if(b2 == 2)		// 副2
						b2 += 25;
				}
				else if(pattern == 3)
				{
					if(a1 == 3 && a2 == 2)// 主2
						a2 += 30;
					else if(a2 == 2)		// 副2
						a2 += 25;
					if(b1 == 3 && b2 == 2)// 主2
						b2 += 30;
					else if(b2 == 2)		// 副2
						b2 += 25;
				}
				else if(pattern == 4)
				{
					if(a1 == 4 && a2 == 2)// 主2
						a2 += 30;
					else if(a2 == 2)		// 副2
						a2 += 25;
					if(b1 == 4 && b2 == 2)// 主2
						b2 += 30;
					else if(b2 == 2)		// 副2
						b2 += 25;
				}
				//最后由花色决定基本牌的大小
				if(a1 == pattern)
					//默认加1，比其他花色都大
					a2 += 1;
				
				//比较牌型
				flag = b2 - a2;
				if (flag == 0)
					return b1 - a1;
				else {
					return flag;
				}
			}
		});
	}

	// 发牌移动卡牌
	public static void move(BeanCard card, Point from, Point to) 
	{
		if(to.x!=from.x)
		{
			double k=(1.0)*(to.y-from.y)/(to.x-from.x);
			double b=to.y-to.x*k;
			int flag=0;//判断向左还是向右移动步幅
			if(from.x<to.x)
				flag=20;
			else 
			{
				flag=-20;
			}
			for(int i=from.x;Math.abs(i-to.x)>20;i+=flag)
			{
				double y=k*i+b;//这里主要用的数学中的线性函数

				card.setLocation(i,(int)y);
				try {
					Thread.sleep(5); //延迟，可自己设置
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
