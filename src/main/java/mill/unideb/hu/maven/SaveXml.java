package mill.unideb.hu.maven;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author dikder
 *
 */
public class SaveXml {
	/**
	 * Logger to debug, log information and warnings.
	 */
	private static Logger logger = LoggerFactory.getLogger(SaveXml.class);
	
	/**
	 * Streamresult.
	 */
	private static StreamResult sr;
	
	/**
	 * The name of the saved game.
	 */
	private static String savedFile;
	
	/**
	 * The name of the folder, which contains the saved game.
	 */
	public static File dir=new File("SavedGame");
	
	/**
	 * Constructor of the SaveXml, save the game's current status.
	 */
	public SaveXml(){
		if(!dir.exists()){
			dir.mkdir();
		}
		try{
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
			Document document = dBuilder.newDocument();
			
			Element rootElement = document.createElement("stones");
			document.appendChild(rootElement);
			
			Element activeSide = document.createElement("activeSide");
			rootElement.appendChild(activeSide);
			if(Game.isPlayer())
				activeSide.setTextContent("true");
			else
				activeSide.setTextContent("false");
			
			Element actualState = document.createElement("actualState");
			rootElement.appendChild(actualState);
			switch(Game.currentState){
			case PLACE:
				actualState.setTextContent("PLACE");
				break;
			case TAKE:
				actualState.setTextContent("TAKE");
				break;
			case MOVE:
				actualState.setTextContent("MOVE");
				break;
			case WIN:
				actualState.setTextContent("WIN");
				break;
			}
			
			Element typeElementDark = document.createElement("type");
			rootElement.appendChild(typeElementDark);	
			typeElementDark.setAttribute("id", "Dark");
			
			Element typeElementLight = document.createElement("type");
			rootElement.appendChild(typeElementLight);
			typeElementLight.setAttribute("id", "Light");
			
			Element numberOfStonesDark=document.createElement("numberOfStones");
			typeElementDark.appendChild(numberOfStonesDark);
			numberOfStonesDark.setTextContent(""+Game.getDark().getNumberOfStones());
			
			Element numberOfStonesLeftToBoardDark=document.createElement("numberOfStonesLeftToBoard");
			typeElementDark.appendChild(numberOfStonesLeftToBoardDark);
			numberOfStonesLeftToBoardDark.setTextContent(""+Game.getDark().getNumberOfStonesLeftToBoard());
			
			Element numberOfStonesLight=document.createElement("numberOfStones");
			typeElementLight.appendChild(numberOfStonesLight);
			numberOfStonesLight.setTextContent(""+Game.getLight().getNumberOfStones());
			
			Element numberOfStonesLeftToBoardLight=document.createElement("numberOfStonesLeftToBoard");
			typeElementLight.appendChild(numberOfStonesLeftToBoardLight);
			numberOfStonesLeftToBoardLight.setTextContent(""+Game.getLight().getNumberOfStonesLeftToBoard());
			
			for(int i=0;i<3;i++){
				for(int j=0;j<8;j++){
					if(Game.stones[i][j]==Game.BLACK){
						Element stone = document.createElement("stone");
						typeElementDark.appendChild(stone);
						Element rectangle = document.createElement("rectangle");
						Element position = document.createElement("position");
						stone.appendChild(rectangle);
						stone.appendChild(position);
						
						rectangle.setTextContent(""+i);
						position.setTextContent(""+j);
					} else if(Game.stones[i][j]==Game.WHITE){
						Element stone = document.createElement("stone");
						typeElementLight.appendChild(stone);
						Element rectangle = document.createElement("rectangle");
						Element position = document.createElement("position");
						stone.appendChild(rectangle);
						stone.appendChild(position);
						
						rectangle.setTextContent(""+i);
						position.setTextContent(""+j);
					}
				}
			}
			
			
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			//String time = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			savedFile = Game.getLight().getName()+"_vs_"+Game.getDark().getName()+".xml";
			sr= new StreamResult(new File(dir+File.separator+Game.getLight().getName()+"_vs_"+Game.getDark().getName()+".xml"));
			
			t.transform(source, sr);
			logger.info("Play was saved succesfully!");
			
		} catch(Exception e){
			logger.error("Failed to create save!");
		}
	}
	
}
