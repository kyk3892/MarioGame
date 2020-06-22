package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Game extends JFrame{

	//더블버퍼링으로 화면 깜빡임 해결
	private Image bufferImage;
	private Graphics screenGraphics;
	
	//이미지들 가져오기
	Image backgroundImg = new ImageIcon("src/Images/marioback.jpg").getImage();
	Image mario = new ImageIcon("src/Images/mario.png").getImage();
	Image mariofood = new ImageIcon("src/Images/mariofood.png").getImage();
	Image poison = new ImageIcon("src/Images/poison.png").getImage();

	//배경, 이미지 위치 이미지 80X80 크기
	private int marioX, marioY;
	private int marioWidth=80,marioHeight=104;
	
	private int mariofoodX, mariofoodY,poisonX,poisonY;
	
	private int gravity,gravity2; //gravity1은 버섯 중력, gravity2는 독 중력
	private int score,heart;
	private boolean left,right,up,die=false;
	
	public static void main(String[] args) {
		new Game();
	}
	public Game() { //생성자 생성
		setTitle("♬A Hungry Mario♬"); //위에 창 타이틀
		setVisible(true); //창보이게
		setSize(850, 850); //창크기
		setLocationRelativeTo(null); //setLocationRelativeTo의 경우 괄호안에 null을 넣으면 실행 시 창이 화면 가운데에 뜸
		setResizable(false); //창크기 변경 불가능하게
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창 닫을시 프로세스 종료
		run();
	}
	
	public void screenDraw(Graphics g) { //Graphics의 drawImage()메소드 사용해서 이미지 출력
		g.drawImage(backgroundImg, 0, 0, null); //배경이미지
		g.drawImage(mario, marioX, marioY, null); //용사 이미지
		g.drawImage(mariofood, mariofoodX, mariofoodY, null); //돈 이미지
		g.drawImage(poison,poisonX, poisonY, null); //독 이미지

		//점수, 목숨 나오게
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("HEART : "+heart,30, 80);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 40)); //점수 글씨체, 폰트, 크기
		g.drawString(score+" : SCORE", 570, 80); //drawString(출력문자, x좌표, y좌표) 설정
		
		if(die==true) { //죽었을때 중력 없애고 게임오버창
			gravity=0;gravity2=0;
			g.setColor(Color.black);
			g.fillRect(30, 212, 790, 425);
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 80));
			g.drawString("----------------------------", 45, 255);
			g.drawString("GAME OVER", 200, 400);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("Your Score : "+score, 270, 500);
			g.setFont(new Font("Arial", Font.BOLD, 80));
			g.drawString("----------------------------", 45, 630);
		}
		this.repaint();
	}
	public void run() {
		first(); //초기화 함수 실행
		keyInsert(); //키 입력받기
		while(true) {
			//대기시간 주기
			try {
				Thread.sleep(10);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			move(); //마리오
			meet(); //뭔가 닿았을 때
		}
	}
	public void keyInsert() {
		addKeyListener(new KeyAdapter() { //키 입력받기
			//키를 눌렀을 때 실행 할 메소드
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					left = true;
					break;
				case KeyEvent.VK_RIGHT:
					right = true;
					break;
				}
			}
			//키를 뗐을 때 실행 할 메소드
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					left = false;
					break;
				case KeyEvent.VK_RIGHT:
					right = false;
					break;
				}
			}
		});
	}
	public void move() { //마리오 움직이기
		if(left) marioX -= 6;
		if(right) marioX += 6;
		
		mariofoodY += gravity; 
		poisonY += gravity2;
		
		if(mariofoodY==830) { //버섯 못 먹고 떨어지는 경우
			mariofoodX = (int)(Math.random()*(850-marioWidth));
			mariofoodY = 30;
			score -= 100;
		}
		if(poisonY==800) { //독 안 먹은 경우
			poisonX = (int)(Math.random()*(850-marioWidth));
			poisonY = 30;
		}
	}
	public void first() { //게임을 시작할 때 초기화
		score=0;
		heart=3;
		
		gravity=8;
		gravity2=7;
		
		marioX = (850-marioWidth)/2; //마리오 가운데
		marioY = (850-marioHeight)-90;
		
		mariofoodX = (int)(Math.random()*(850-80)); //버섯 랜덤위치 
		mariofoodY = 30; //위의 창 길이
		
		poisonX = (int)(Math.random()*850-80); //독 랜덤위치 
		poisonY = 30; //위의 창 길이
		
	}
	public void meet() { //버섯, 독 먹었을 때
		if(marioX+marioWidth > mariofoodX && mariofoodX+80 > marioX && 
				marioY + marioHeight > mariofoodY && mariofoodY + 80 > marioY) { //버섯 먹었을 때
			 score += 200;
			 mariofoodX = (int)(Math.random()*850-80); //버섯 다시 나오기	
			 mariofoodY = 30;
		}
		if(marioX+marioWidth > poisonX && poisonX+80 > marioX && 
				marioY + marioHeight > poisonY && poisonY + 80> marioY) { //독 먹었을 때
			score -= 100;
			heart--;
			if(heart==0) {
				die = true;
			}
			poisonX = (int)(Math.random()*850-80);
			poisonY = 30;
		}
	}
	public void paint(Graphics g) { //더블 버퍼링으로 깜빡임 해결하기
		bufferImage = createImage(850,850);
		screenGraphics = bufferImage.getGraphics();
		screenDraw(screenGraphics); //버퍼 이미지를 화면에 그려주는 작업
		g.drawImage(bufferImage, 0, 0, null);
	}
	
	
}
