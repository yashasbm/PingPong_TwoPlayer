import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener {
	public static Pong pong;
	public int width = 1200, height = 800;
	public Renderer renderer;
	public Paddle player1;
	public Paddle player2;
	public Ball ball;
	public boolean w, s, up, down;
	public int gameStatus = 0, maxScore = 5, playerWon; // 0 = Menu, 1 = Paused, 2 = Playing, 3 = Over
	public Random random;
	public JFrame jframe;

	public Pong() {
		Timer timer = new Timer(20, this);
		random = new Random();
		jframe = new JFrame("Ping Pong");
		renderer = new Renderer();
		jframe.setSize(width + 15, height + 35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);
		timer.start();
	}

	public void start() {
		gameStatus = 2;
		player1 = new Paddle(this, 1);
		player2 = new Paddle(this, 2);
		ball = new Ball(this);
	}

	public void update() {
		if (player1.score >= maxScore) {
			playerWon = 1;
			gameStatus = 3;
		}
		if (player2.score >= maxScore) {
			gameStatus = 3;
			playerWon = 2;
		}
		if (w) {
			player1.move(true);
		}
		if (s) {
			player1.move(false);
		}
		if (up) {
			player2.move(true);
		}
		if (down) {
			player2.move(false);
		}
		ball.update(player1, player2);
	}

	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (gameStatus == 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", 1, 50));
			g.drawString("PING PONG", width / 2 - 120, 50);
			if (gameStatus == 0) {
				g.setFont(new Font("Courier", 1, 30));
				g.drawString("Press Space to Start", width / 2 - 150, height / 2 - 25);
				g.drawString("Maximum Score: " + maxScore, width / 2 - 150, height / 2 + 75);
			}
		}
		if (gameStatus == 1) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", 1, 50));
			g.drawString("PAUSED!", width / 2 - 103, height / 2 - 25);
		}
		if (gameStatus == 1 || gameStatus == 2) {
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(5f));
			g.drawLine(width / 2, 0, width / 2, height);
			g.setStroke(new BasicStroke(2f));
			g.setFont(new Font("Courier", 1, 50));
			g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
			g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);
			player1.render(g);
			player2.render(g);
			ball.render(g);
		}
		if (gameStatus == 3) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", 1, 50));
			g.drawString("PING PONG", width / 2 - 75, 50);
			g.setFont(new Font("Courier", 1, 30));
			g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
			g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameStatus == 2) {
			update();
		}
		renderer.repaint();
	}

	public static void main(String[] args) {
		pong = new Pong();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_W) {
			w = true;
		} else if (id == KeyEvent.VK_S) {
			s = true;
		} else if (id == KeyEvent.VK_UP) {
			up = true;
		} else if (id == KeyEvent.VK_DOWN) {
			down = true;
		} else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3)) {
			gameStatus = 0;
		} else if (id == KeyEvent.VK_SPACE) {
			if (gameStatus == 0 || gameStatus == 3) {
				start();
			} else if (gameStatus == 1) {
				gameStatus = 2;
			} else if (gameStatus == 2) {
				gameStatus = 1;
			}
		} else if (gameStatus == 1) {
			gameStatus = 2;
		} else if (gameStatus == 2) {
			gameStatus = 1;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_W) {
			w = false;
		} else if (id == KeyEvent.VK_S) {
			s = false;
		} else if (id == KeyEvent.VK_UP) {
			up = false;
		} else if (id == KeyEvent.VK_DOWN) {
			down = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
