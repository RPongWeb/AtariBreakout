package atariBreakout;

import java.awt.Color; 
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.awt.color.*;
import javax.swing.JPanel;
import java.awt.Graphics2D;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310;
	private int ballposX = 350;
	private int ballposY = 500;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	
	public Gameplay(){
		map = new MapGenerator(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g){
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		map.draw((Graphics2D)g);
		
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		g.setColor(Color.white);
		g.setFont(new Font("Calibri", Font.BOLD, 32));
		g.drawString("Current Score: "+ score, 440, 30);
		
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);		
	
		if(totalBricks <= 0){
				play = false;
				ballXdir = 0;
				ballYdir = 0;
				g.setColor(Color.RED);
				g.setFont(new Font("Helvetica", Font.BOLD, 48));
				g.drawString("You Win!, Score: "+ score, 100,300);
				g.setFont(new Font("Helvetica", Font.BOLD, 36));
				g.drawString("Press Enter to Restart!", 150, 400);
		}
		
		if(ballposY > 570){
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("Helvetica", Font.BOLD, 48));
			g.drawString("Game Over, Score:"+ score, 90,300);
			g.setFont(new Font("Helvetica", Font.BOLD, 36));
			g.drawString("Press Enter to Restart!", 150, 400);
		}
		
		g.dispose();
	}
	


	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play){
			if(new Rectangle(ballposX, ballposY , 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
				ballYdir = -ballYdir;
			}
			A: for(int i = 0; i < map.map.length; i++){
				for (int j = 0; j < map.map[0].length;j++){
					if(map.map[i][j] > 0){
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)){
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += getRandomNumber();
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
								ballXdir = -ballXdir;
							}
							else{
								ballYdir = -ballYdir;
							}
							
							break A;
						}
					}
				}
			}
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0){
				ballXdir = -ballXdir;
			}
			if(ballposY < 0){
				ballYdir = -ballYdir;
			}
			if(ballposX > 670){
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}

	public int getRandomNumber() {
		return (int)(Math.random()*5 + 5);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(playerX >= 600){
				playerX = 600;
			}
			else{
				moveRight();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			if(playerX < 10){
				playerX = 10;
			}
			else{
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(!play){
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 20;
				map = new MapGenerator(3,7);
				
				repaint();
			}
		}
	}

	public void moveRight() {
		play = true;
		playerX += 20;
	}

	public void moveLeft() {
		play = true;
		playerX -= 20;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
}
