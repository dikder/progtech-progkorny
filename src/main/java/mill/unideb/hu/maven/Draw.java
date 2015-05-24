package mill.unideb.hu.maven;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * 
 * @author dikder
 *
 */
public class Draw extends JComponent implements GameController{

	private Rectangle outer, middle, inner;

	public boolean move;

	private int stonei = -1;
	private int stonej = -1;
	
	private int besti = 0;


	private int bestj = 0;
	


	int[][] stones=new int[3][8];

	public Draw() {
		this.setOpaque(true);

		
		this.outer = null;
		this.middle = null;
		this.inner = null;
	}

	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		Insets insets = this.getInsets();
		Rectangle screen = new Rectangle(insets.left, insets.top,
				this.getWidth() - insets.right - insets.left, this.getHeight()
						- insets.bottom - insets.top);

		g.setColor(new Color(240, 120, 80));
		g.fillRect(screen.x, screen.y, screen.width, screen.height);

		g.setColor(Color.white);

		// Draw the background color rectangles
		final int borderWidth = 15;
		final double insetAmount = 0.16666;
		final int pieceSize = 20;
		final float lineWidth = 5.0f;

		outer = (Rectangle) screen.clone();
		outer.grow(-borderWidth, -borderWidth);

		middle = (Rectangle) outer.clone();
		middle.grow((int) (-outer.width * insetAmount),
				(int) (-outer.height * insetAmount));

		inner = (Rectangle) middle.clone();
		inner.grow((int) (-outer.width * insetAmount),
				(int) (-outer.height * insetAmount));

		g.setColor(Color.black);
		g.setStroke(new BasicStroke(lineWidth));

		// Draw the rectangles
		g.draw(outer);
		g.draw(middle);
		g.draw(inner);

		// Draw the connecting lines
		g.drawLine(outer.x + (outer.width / 2), outer.y, outer.x
				+ (outer.width / 2), inner.y);
		g.drawLine(outer.x + (outer.width / 2), outer.y + outer.height, outer.x
				+ (outer.width / 2), inner.y + inner.height);
		g.drawLine(outer.x, outer.y + (outer.height / 2), inner.x, outer.y
				+ (outer.height / 2));
		g.drawLine(outer.x + outer.width, outer.y + (outer.height / 2), inner.x
				+ inner.width, outer.y + (outer.height / 2));

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				int x=getStoneX(i, j);
				int y=getStoneY(i, j);
				
				if (this.stones[i][j] == Game.WHITE) {
					g.setColor(Color.white);
				} else if (this.stones[i][j] == Game.BLACK) {
					g.setColor(Color.black);
				}
				if (this.stones[i][j] != Game.EMPTY) {
					g.fillOval(x - (pieceSize / 2), y - (pieceSize / 2),
							pieceSize, pieceSize);
					g.drawOval(x - (pieceSize / 2), y - (pieceSize / 2),
							pieceSize, pieceSize);
				}
				if (move && stonei == i && stonej == j) {
					g.setStroke(new BasicStroke(2.0f));
					g.setColor(Color.cyan);
					g.drawRect(x - ((pieceSize + 2) / 2), y
							- ((pieceSize + 2) / 2), pieceSize + 4,
							pieceSize + 4);

					stonei=-1;
					stonej=-1;
				}

			}
		}
		
		
		
	}

	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	private int getStoneX(int rect, int stone) {
		int x = 0;
		if (rect == 0) {
			switch (stone) {
			case 0:
			case 6:
			case 7:
				x = outer.x;
				break;
			case 1:
			case 5:
				x = outer.x + outer.width / 2;
				break;
			case 2:
			case 3:
			case 4:
				x = outer.x + outer.width;
				break;
			}
		}
		if (rect == 1) {
			switch (stone) {
			case 0:
			case 6:
			case 7:
				x = middle.x;
				break;
			case 1:
			case 5:
				x = middle.x + middle.width / 2;
				break;
			case 2:
			case 3:
			case 4:
				x = middle.x + middle.width;
				break;
			}
		}
		if (rect == 2) {
			switch (stone) {
			case 0:
			case 6:
			case 7:
				x = inner.x;
				break;
			case 1:
			case 5:
				x = inner.x + inner.width / 2;
				break;
			case 2:
			case 3:
			case 4:
				x = inner.x + inner.width;
				break;
			}
		}

		return x;
	}

	private int getStoneY(int rect, int stone) {
		int y = 0;
		if (rect == 0) {
			switch (stone) {
			case 0:
			case 1:
			case 2:
				y = outer.y;
				break;
			case 3:
			case 7:
				y = outer.y + outer.height / 2;
				break;
			case 4:
			case 5:
			case 6:
				y = outer.y + outer.height;
				break;
			}
		}
		if (rect == 1) {
			switch (stone) {
			case 0:
			case 1:
			case 2:
				y = middle.y;
				break;
			case 3:
			case 7:
				y = middle.y + middle.height / 2;
				break;
			case 4:
			case 5:
			case 6:
				y = middle.y + middle.height;
				break;
			}
		}
		if (rect == 2) {
			switch (stone) {
			case 0:
			case 1:
			case 2:
				y = inner.y;
				break;
			case 3:
			case 7:
				y = inner.y + inner.height / 2;
				break;
			case 4:
			case 5:
			case 6:
				y = inner.y + inner.height;
				break;
			}
		}

		return y;
	}
	
	
	
	public void bestDistance(int x2,int y2) {
		
		int bestDist = Integer.MAX_VALUE;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				int x = this.getStoneX(i, j);
				int y = this.getStoneY(i, j);

				int dist = ((x - x2) * (x - x2) + (y - y2) * (y - y2));
				if (dist < bestDist) {
					bestDist = dist;
					besti = i;
					bestj = j;
				}
			}
		}
	}

	@Override
	public void mouse(int[][] t) {
		this.stones=t;
		repaint();
	}
	
	@Override
	public void move(int fromi, int fromj) {
		this.stonei=fromi;
		this.stonej=fromj;
		this.move=true;
		repaint();
	}

	@Override
	public void win(Player winner) {
		 JOptionPane.showMessageDialog(this,
				  winner.getName() + " wins.", "Game over",
				  JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public int getBesti() {
		return besti;
	}	

	public int getBestj() {
		return bestj;
	}
	

}
