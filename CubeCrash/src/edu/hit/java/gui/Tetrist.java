package edu.hit.java.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tetrist extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		final JFrame frame = new JFrame("Tetris Game");
		final GamePapel a = new GamePapel();
		frame.addKeyListener(a);
		frame.add(a);	
		a.timer.start();
		JMenuBar menu = new JMenuBar();
		// help menu
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutitem = new JMenuItem("About ");
		aboutitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.showHelp();
			}
		});
		
		helpMenu.add(aboutitem);
		menu.add(helpMenu);
		frame.setJMenuBar(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(300, 100, 400, 420);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}

// ����һ������˹������
class GamePapel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	private boolean pause=false;
	final Timer timer = new Timer(500, new TimerListener());
	Random ran = new Random(); // ʹ��Random������
	private int TetriminoType; // ����������
	private int score = 0; // ������
	private int rotateState; //������״̬
	private int x,y; // ������ʼλ�õ�����
	private int nextb = ran.nextInt(7); // ��һ���������ͣ�
	private int nextt = ran.nextInt(4); // ��һ���������״��

	int[][] map = new int[23][13];

	GamePapel() {
		newTetrimino();
		newMap();
	}

	// ��һά���������״��������״������S�� Z�� L�� J�� I�� O�� T 7��
	// �ڶ�ά������״̬
	// ����άΪ�������
	private final int shapes[][][] = new int[][][] {
			// ���ͷ���
			{ 
				{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } 
			},
			// s�ͷ���
			{ 
				{ 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } 
			},
			// z�ͷ���
			{ 
				{ 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
			// ��l�ͷ���
			{
				{ 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 }, 
				{ 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
			// ���ͷ���
			{ 
				{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
			// ��l�ͷ���
			{
				{ 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
			// t�ͷ���
			{ 
				{ 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 } 
			} 
	};
		
	// initialize the map
	public void newMap() {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 23; j++) {
				map[j][i] = 0;
			}
		}
		// draw walls
		for (int i = 0; i < 12; i++) {//right-1
			map[21][i] = 2;
		}
		for (int j = 0; j < 22; j++) {//lower-1
			map[j][11] = 2;
			map[j][0] = 2;
		}
	}


	//new Tetrimino
	public void newTetrimino() {
		TetriminoType = nextb;
		rotateState = nextt;
		nextb = ran.nextInt(7);
		nextt = ran.nextInt(4);
		x = 4;
		y = 0;
		if (collisionDetect(x, y, TetriminoType, rotateState)) {
			JOptionPane.showMessageDialog(null, "Game Over!");
			newMap();
			score = 0;
		}
	}

	//collision Detect
	public boolean collisionDetect(int x, int y, int blockType, int turnState) {
		//a,bΪ�������(ÿ����״��ÿ��״̬)
		for (int a = 0; a < 4; a++) {
			for (int b = 0; b < 4; b++) {
				if (((shapes[blockType][turnState][a * 4 + b] == 1) 
						&& (map[y + a][x + b + 1] == 1))|| 
					((shapes[blockType][turnState][a * 4 + b] == 1) 
						&& (map[y + a][x + b + 1] == 2))) {
					return true;
				}
			}
		}
		return false;
	}
	public void showHelp(){
		JOptionPane.showMessageDialog(null,
				"Welcole to play Tetris Game!\nEscape Start/Pause\n",
				"Ablout", JOptionPane.INFORMATION_MESSAGE);		
	}
	

	// ��ת�ķ���
	private void rotate() {
		int tempturnState = rotateState;
		rotateState = (rotateState + 1) % 4;
		if (pause||collisionDetect(x, y, TetriminoType, rotateState)) {
			rotateState = tempturnState;
		}
	}

	// ���Ƶķ���
	private void left() {
		if (!pause&&!collisionDetect(x - 1, y, TetriminoType, rotateState)) {
			x = x - 1;
		}
	}

	// ���Ƶķ���
	private void right() {
		if (!pause&&!collisionDetect(x + 1, y, TetriminoType, rotateState)) {
			x = x + 1;
		}
	}

	// ����ķ���
	private void fall() {
		if (!pause&&!collisionDetect(x, y + 1, TetriminoType, rotateState)) {
			y = y + 1;
		}
		
	}

	// ���еķ���
	private void clearLines() {
		int c = 0;
		int lines = 0; // ����ȷ���������˼���
		for (int yy = 0; yy < 22; yy++) {
			c = 0;
			for (int xx = 0; xx < 12; xx++) {
				if (map[yy][xx] == 1) {
					c = c + 1;
					if (c == 10) {
						lines++;
						for (int cy = yy; cy > 0; cy--) {
							for (int e = 1; e < 11; e++) {
								map[cy][e] = map[cy - 1][e];
							}
						}
					}
				}
			}
		}
		// ȷ�����з���
		switch (lines) {
		case 1:
			score = score + 10;
			break;
		case 2:
			score = score + 40;
			break;
		case 3:
			score = score + 90;
			break;
		case 4:
			score = score + 160;
			break;
		default:
			break;
		}
	}

	// �ѵ�ǰ���map
	private void addToMap(int x, int y, int blockType, int turnState) {
		int j = 0;
		for (int a = 0; a < 4; a++) {
			for (int b = 0; b < 4; b++) {
				if (map[y + a][x + b + 1] == 0) {
					map[y + a][x + b + 1] = shapes[blockType][turnState][j];
				}
				j++;
			}
		}
	}

	// ������ĵķ���
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		// ����ǰ����
		for (int j = 0; j < 16; j++) {
			if (shapes[TetriminoType][rotateState][j] == 1) {
				// ����������
				g.setColor(Color.BLUE);
				g.fill3DRect((j % 4 + x + 1) * 20, (j / 4 + y) * 20, 20, 20, true);

			}
		}
		// ����һ������(�Ҳ�)
		for (int j = 0; j < 16; j++) {
			if (shapes[nextb][nextt][j] == 1) {
				g.setColor(Color.BLUE);
				g.fill3DRect((j % 4 + 1) * 20 + 250, (j / 4) * 20 + 40, 20, 20, true);

			}
		}
		// ���Ѿ��̶��ķ���
		for (int j = 0; j < 22; j++) {
			for (int i = 0; i < 12; i++) {
				if (map[j][i] == 2) { // ��Χǽ
					g.setColor(Color.BLACK);
					g.fill3DRect(i * 20, j * 20, 20, 20, true);

				}
				if (map[j][i] == 1) { // ���̶��ķ���
					g.setColor(Color.GREEN);
					g.fill3DRect(i * 20, j * 20, 20, 20, true);

				}
			}
		}
		//����������ɫ
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		g.drawString("Score = " + score, 250, 10);
		//����
		g.setColor(Color.RED);
		g.drawString("Next Tetrimino: ", 250, 30);
	}

	// ���̼���
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			fall();
			break;
		case KeyEvent.VK_UP:
			rotate();
			break;
		case KeyEvent.VK_RIGHT:
			right();
			break;
		case KeyEvent.VK_LEFT:
			left();
			break;
		case KeyEvent.VK_ESCAPE:
			if (!pause){
				pause=true;
				timer.stop();
			}else{
				pause=false;
				timer.start();	
			}
			break;
		default:
			break;
		}
		repaint();

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fall();
			if (collisionDetect(x, y + 1, TetriminoType, rotateState)) {
					addToMap(x, y, TetriminoType, rotateState);
					clearLines();
					newTetrimino();
			}
			repaint();
		}
	}
}
