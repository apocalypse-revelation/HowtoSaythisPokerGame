package Client;

import java.util.List;

public class BeanPlayer {
	
	String name;//�������
	boolean isBoss;//ׯ����
	boolean isLocal;//���ڲ�������ң�
	int playNumber;//������
	int cardNumber;//�����������
	List<BeanCard> cardList;//��ҳ�������list
	int score;//��ҵ÷�
	
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
