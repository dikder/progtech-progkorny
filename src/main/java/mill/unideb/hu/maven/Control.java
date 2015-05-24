package mill.unideb.hu.maven;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author dikder
 */
public class Control{
	Game game;
	GUImill gui;
	GuiService service;

	public Control(Player light, Player dark) {
		this.gui = new GUImill();
		this.game = new Game(light, dark);
		service=game;
		service.guiservice(gui);
		
		gui.getDraw().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && !e.isConsumed()) {
					e.consume();

						gui.draw.bestDistance(e.getX(), e.getY());
						game.setBesti(gui.draw.getBesti());
						game.setBestj(gui.draw.getBestj());
						game.play();

				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
	}


}
