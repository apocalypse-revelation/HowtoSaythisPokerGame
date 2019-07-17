package Client;

import java.util.List;

public class BeanPlayer {
	
	String name;//玩家姓名
	boolean isBoss;//庄主？
	boolean isLocal;//正在操作的玩家？
	int playNumber;//玩家序号
	int cardNumber;//玩家手牌数量
	List<BeanCard> cardList;//玩家持有手牌list
	int score;//玩家得分
	
	public BeanPlayer(String name, int playNumber) {
		super();
		this.name = name;
		this.playNumber = playNumber;
		this.isLocal = false;
		this.isBoss = false;
	}
	
	public void setCards(List<BeanCard> list){
		this.cardNumber+=list.size();
		this.cardList.addAll(list);
	}

	public String getName() {
		return name;
	}

	public boolean isBoss() {
		return isBoss;
	}

	public int getPlayNumber() {
		return playNumber;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public List<BeanCard> getCardList() {
		return cardList;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
