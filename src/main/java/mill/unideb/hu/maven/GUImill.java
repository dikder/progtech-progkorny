package mill.unideb.hu.maven;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.peer.LightweightPeer;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dikder
 *
 */
public class GUImill extends JFrame implements ActionListener, StatusController {

	
	private JPanel contentPane;

	private static Logger logger = LoggerFactory.getLogger(GUImill.class);

	public JMenuBar menubar;

	private JMenu menu;

	private JMenuItem newGame;

	private JMenuItem exit;

	private JMenuItem save;

	private static int boardSize = 600 - 60;


	private static Image img;

	private JPanel boardPanel;

	private JButton millSquares[][] = new JButton[3][8];

	private int width = 600;

	private int height = 600;

	private Draw board;
	
	private Player light;
	
	private Player dark;
	
	private JLabel label;
	
	private JLabel statusLabel;
	
	private JLabel lightNameLabel;
	
	private JLabel darkNameLabel;
	
	private JLabel whiteScore1;
	
	private JLabel whiteScore2;
	
	private JLabel blackScore1;
	
	private JLabel blackScore2;
	
	JPanel barPane;
	
	Draw draw;

	public Draw getDraw() {
		return draw;
	}

	public GUImill() {
		this.draw=new Draw();
		
		initGui();
	}

	private void createMenu() {
		menubar = new JMenuBar();
		menu = new JMenu("Game");
		menubar.add(menu);

		newGame = new JMenuItem("New Game");
		menu.add(newGame);

		save = new JMenuItem("Save");
		menu.add(save);

		exit = new JMenuItem("Exit");
		menu.add(exit);

		setJMenuBar(menubar);
		newGame.addActionListener(this);
		exit.addActionListener(this);
		save.addActionListener(this);
	}

	private void initGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Mill");
		this.createMenu();

		
		getContentPane().add(draw, BorderLayout.CENTER);

		barPane = new JPanel(new BorderLayout());
		barPane.setBackground(Color.white);
		barPane.setPreferredSize(new Dimension(130, 400));
		barPane.setOpaque(true);

		JPanel scorePane = new JPanel(new GridBagLayout());
		scorePane.setBackground(Color.white);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		label = new JLabel(
				"<html><b><font size=4>Scores</font></b><br></html>",
				JLabel.CENTER);
		scorePane.add(label, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		lightNameLabel = new JLabel("White", JLabel.LEFT);
		scorePane.add(lightNameLabel, c);
		
		c.gridy=2;
		whiteScore1=new JLabel(0+" on table",JLabel.CENTER);
		scorePane.add(whiteScore1, c);
		
		c.gridy=3;
		whiteScore2=new JLabel(9+" in stack",JLabel.CENTER);
		scorePane.add(whiteScore2, c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0;
		darkNameLabel = new JLabel("Black",JLabel.LEFT);
		scorePane.add(darkNameLabel,c);
		
		c.gridy=5;
		blackScore1=new JLabel(0+" on table",JLabel.CENTER);
		scorePane.add(blackScore1, c);
		
		c.gridy=6;
		blackScore2=new JLabel(9+" in stack",JLabel.CENTER);
		scorePane.add(blackScore2, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.ipady = 20;
		c.gridwidth = 2;
		statusLabel=new JLabel("",JLabel.LEFT);
		scorePane.add(statusLabel,c);

		barPane.add(scorePane, BorderLayout.NORTH);

		getContentPane().add(barPane, BorderLayout.LINE_END);
		
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==newGame){
			this.dispose();
			Names n=new Names();
			n.darkNameField.setText("");
			n.lightNameField.setText("");
		} else
			if(e.getSource()==exit){
				this.dispose();
	        	System.exit(0);
			} else
				if(e.getSource()==save){
					new SaveXml();
				}

	}
	
	public void refresh(Player player1, Player player2) {
		whiteScore1.setText(player1.getNumberOfStones()-player1.getNumberOfStonesLeftToBoard()+
				" on table");
		whiteScore2.setText(9-player1.getNumberOfStones()+	" in stack");
		blackScore1.setText(player2.getNumberOfStones()-player2.getNumberOfStonesLeftToBoard()+
				" on table");
		blackScore2.setText(9-player2.getNumberOfStones()+	" in stack");
	}
	
	@Override
	public void nameUpdate(String light, String dark) {
		lightNameLabel.setText("White"+" ("+light+"):");
		darkNameLabel.setText("Black"+" ("+dark+"):");
		
	}
	
	@Override
	public void statusUpdate(String text) {
		statusLabel.setText(text);
	}

}
