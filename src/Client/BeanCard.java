package Client;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BeanCard extends JLabel implements MouseListener {

	public String name;// ͼƬurl
	public int pattern;//��ɫ
	public int points;//����
	public boolean up;// ������
	public boolean canClick = false;// �Ƿ�ɱ����
	public boolean clicked = false;// �Ƿ�����

	public BeanCard(String name, boolean up) {
		this.name = name;
		String [] s = name.split("\\-");
		this.pattern = Integer.parseInt(s[0]);
		this.points = Integer.parseInt(s[1]);
		this.up = up;
		if (this.up)
			this.turnFront();
		else {
			this.turnRear();
		}
		this.setSize(71, 96);
		this.setVisible(true);
		this.addMouseListener(this);
	}

	public String getName() {
		return name;
	}

	// ����
	public void turnFront() {
		this.setIcon(new ImageIcon("images/" + name + ".gif"));
		this.up = true;
	}

	// ����
	public void turnRear() {
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.up = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (canClick) {
			Point from = this.getLocation();
			int step; // �ƶ��ľ���
			if (clicked)
				step = -20;
			else {
				step = 20;
			}
			clicked = !clicked; // ����
			this.setLocation(new Point(from.x,from.y-step));
		}
	}

	public int getPattern() {
		return pattern;
	}

	public int getPoints() {
		return points;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
