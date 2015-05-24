package mill.unideb.hu.maven;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author dikder
 *
 */
public class Menu extends JFrame implements ActionListener {
	
	/**
	 * Logger to debug, log information and warnings.
	 */
	private static Logger logger = LoggerFactory.getLogger(Menu.class);
	
	private JFileChooser fc = new JFileChooser();

	private JButton newGame;
	
	private JButton load;
	
	private JButton exit;

	private JPanel buttonPanel;
	
	private GridBagLayout buttonPanelLayout;
	
	private GridBagConstraints gbc;

	public Menu(){
		initMenu();
	}
	
	
	private void initMenu() {
		setBounds(500,500,200,200);
		setLocationRelativeTo(null);
		setTitle("Mill");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		setResizable(false);
		
		gbc = new GridBagConstraints();
		buttonPanelLayout = new GridBagLayout();
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(buttonPanelLayout);
		
		add(buttonPanel);
		
		newGame=new JButton("New Game");
		newGame.addActionListener(this);
		
		load=new JButton("Load");
		load.addActionListener(this);
		
		exit=new JButton("Exit");
		exit.addActionListener(this);
		
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 3;
		buttonPanel.add(newGame,gbc);
		
		//gbc.insets = new Insets(100, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 4;
		buttonPanel.add(load,gbc);
		
		//gbc.insets = new Insets(150, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 5;
		buttonPanel.add(exit,gbc);
		
		setVisible(true);
	}


	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==newGame){
			Names n=new Names();
			this.dispose();
		} else
			if(e.getSource()==exit){
				this.dispose();
	        	System.exit(0);
			} else
				if(e.getSource()==load){
					int returnVal = fc.showOpenDialog(this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String[] s=file.getName().split("_vs_");
						Control c=new Control(new Player(s[0]), new Player(s[1].split(".xml")[0]));
						new LoadXml(file.getName());
						this.dispose();
						
					}
				}
		
		
		
	}

}
