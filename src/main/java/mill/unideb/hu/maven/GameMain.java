package mill.unideb.hu.maven;

import javax.swing.SwingUtilities;


/**
 * 
 * @author dikders
 *	Main class of the game.
 */
public class GameMain {
	
	/**
	 * Main method, running the game.
	 * @param args is program parameter, it is not needed now.
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Menu m=new Menu();
			}
		});
		
		
	}

}
