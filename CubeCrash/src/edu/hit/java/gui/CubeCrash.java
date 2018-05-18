package edu.hit.java.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CubeCrash extends JFrame {
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		final JFrame frame = new JFrame("CubeCrash");
		final Cube a = new Cube();//ʵ����
		frame.addMouseListener(a);//������
		frame.add(a);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(300, 100, 400, 320);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}

class Cube extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	final Timer timer = new Timer(100, new TimerListener());//��ʱ��
	Random ran = new Random();
	private int score = 0;
	final int red=1,orange=2,green=3,blue=4,non=0;
	final int line=10,col=10;//������������һ������ȫ�ָ���
	int[][][] map = new int[col][line][2];

	Cube() {
		newMap();
	}
	
	public void newMap() {//��ʼ��
		for(int i=0;i<col;i++)
			for(int j=0;j<line;j++){
				map[i][j][0]=ran.nextInt(4)+1;
				map[i][j][1]=0;
				}	
	}

	private void deletCube(int x,int y){//ʵ���������������Ϸ��������
		int count=findsameCube(x,y);//Ѱ��ͬɫ����
		if(count>1){//�Ƿ������
			for(int i=0;i<col;i++)
				for(int j=0;j<line;j++)
					if(map[i][j][1]==1)map[i][j][0]=non;
			score+=Math.pow(2,count);
		}else{
			map[x][y][1]=0;
		}
		
		repaint();
		timer.start();//��ʼ����
		checkover();//����Ƿ������Ϸ
	}
	
	private void checkover(){//���з�����������ͬɫ����Ϸ����
		for(int i=0;i<col-1;i++)
			for(int j=line-1;j>0;j--){
				if(map[i][j][0]!=non
					&&(map[i][j][0]==map[i+1][j][0]
					||map[i][j][0]==map[i][j-1][0]))return;
			}
		JOptionPane.showMessageDialog(null, "Game Over!");
		newMap();
		score = 0;
	}
	
	private int findsameCube(int x,int y){//�ݹ�������ͬɫ�飬��Ҫ�㷨
		int count=1;
		map[x][y][1]=1;
		if(x>0&&map[x][y][0]==map[x-1][y][0]&&map[x-1][y][1]==0)
			count+=findsameCube(x-1, y);
		if(y>0&&map[x][y][0]==map[x][y-1][0]&&map[x][y-1][1]==0)
			count+=findsameCube(x, y-1);
		if(x<col-1&&map[x][y][0]==map[x+1][y][0]&&map[x+1][y][1]==0)
			count+=findsameCube(x+1, y);
		if(y<line-1&&map[x][y][0]==map[x][y+1][0]&&map[x][y+1][1]==0)
			count+=findsameCube(x, y+1);
		
		return count;
	}
	
	private boolean checkCube(){//����Ƿ��и���ɫ�飬�еĻ�ÿ���½�һ���γɶ���
		boolean flag=true;
		
		for(int i=0;i<col;i++)
			for(int j=0;j<line-1;j++)
				if(map[i][j][0]!=non&&map[i][j+1][0]==non)
				{
					flag=false;
					swap(i,j+1,i,j);
				}
		for(int i=0;i<col-1;i++){
			int j;
			for(j=0;j<line;j++){
				if(map[i][j][0]!=non)break;
			}
			if(j==line){
				flag=false;
				for(j=0;j<line;j++){
					swap(i,j,i+1,j);
				}
			}
		}
		return flag;
	}
	
	public void swap(int x1,int y1,int x2,int y2){
		int [][][]temp=new int[1][1][2];
		temp[0][0]=map[x1][y1];
		map[x1][y1]=map[x2][y2];
		map[x2][y2]=temp[0][0];
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i=0;i<col;i++)
			for(int j=0;j<line;j++){
				boolean flag=true;
				switch(map[i][j][0]){
				case green:g.setColor(Color.GREEN);break;
				case orange:g.setColor(Color.ORANGE);break;
				case red:g.setColor(Color.RED);break;
				case blue:g.setColor(Color.BLUE);break;
				default:flag=false;break;
				}
				if(flag)g.fill3DRect(i * 20+20, j * 20+20, 20, 20, true);
			}

		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		g.drawString("Score = " + score, 250, 100);
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1){//���������  
			int x=e.getX();
			int y=e.getY();
			if(x>20&&x<20*col+20&&y>40&&y<line*20+40)deletCube((x-20)/20,(y-40)/20);
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			if(checkCube()){
				timer.stop();
			}
			repaint();
		}
	}
	
}