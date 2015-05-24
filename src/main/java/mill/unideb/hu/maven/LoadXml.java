package mill.unideb.hu.maven;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author dikder
 *
 */
public class LoadXml {

	/**
	 * Logger to debug, log information and warnings.
	 */
	private static Logger logger = LoggerFactory.getLogger(LoadXml.class);

	/**
	 * Name of the input file, which contains the name, type and position
	 * information.
	 */
	private static String fileName;

	/**
	 * Constructor of the LoadXml, initialize base matrices.
	 * 
	 * @param fileName is the input file of names, types and positions.
	 */
	public LoadXml(String fileName) {
		setFileName(fileName);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				Game.stones[i][j] = Game.EMPTY;
			}
		}
		readXml(getFileName());
	}

	/**
	 * Reading the input file and fill the matrices with the proper datas.
	 * 
	 * @param fileName
	 *            is the input file.
	 */
	private void readXml(String fileName) {

		try {
			File file = new File("SavedGame" + File.separator + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			logger.info("Reading xml file: " + getFileName());

			Node node = doc.getElementsByTagName("activeSide").item(0);
			Element e = (Element) node;
			if (e.getTextContent().compareTo("true") == 0)
				Game.setPlayer(true);
			else
				Game.setPlayer(false);

			node = doc.getElementsByTagName("actualState").item(0);
			e = (Element) node;

			switch (e.getTextContent()) {
			case "TAKE":
				Game.currentState = Game.GameState.TAKE;
				break;
			case "MOVE":
				Game.currentState = Game.GameState.MOVE;
				break;
			case "PLACE":
				Game.currentState = Game.GameState.PLACE;
				break;
			case "WIN":
				Game.currentState = Game.GameState.WIN;
				break;
			}

			NodeList numberOfStones = doc
					.getElementsByTagName("numberOfStones");
			for (int i = 0; i < numberOfStones.getLength(); i++) {
				Node nodes = numberOfStones.item(i);

				if (nodes.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodes.getParentNode();
					if (element.getAttribute("id").toString().compareTo("Dark") == 0) {
						Element e1 = (Element) nodes;
						Game.getDark().setNumberOfStones(
								Integer.parseInt(e1.getTextContent()));
					} else if (element.getAttribute("id").toString()
							.compareTo("Light") == 0) {
						Element e1 = (Element) nodes;
						Game.getLight().setNumberOfStones(
								Integer.parseInt(e1.getTextContent()));
					}
				}
			}

			NodeList numberOfStonesLeftToBoard = doc
					.getElementsByTagName("numberOfStonesLeftToBoard");
			for (int i = 0; i < numberOfStonesLeftToBoard.getLength(); i++) {
				Node nodes = numberOfStonesLeftToBoard.item(i);

				if (nodes.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodes.getParentNode();
					if (element.getAttribute("id").toString().compareTo("Dark") == 0) {
						Element e1 = (Element) nodes;
						Game.getDark().setNumberOfStonesLeftToBoard(
								Integer.parseInt(e1.getTextContent()));
					} else if (element.getAttribute("id").toString()
							.compareTo("Light") == 0) {
						Element e1 = (Element) nodes;
						Game.getLight().setNumberOfStonesLeftToBoard(
								Integer.parseInt(e1.getTextContent()));
					}
				}
			}

			NodeList stoneList = doc.getElementsByTagName("stone");

			for (int i = 0; i < stoneList.getLength(); i++) {
				Node nodes = stoneList.item(i);

				if (nodes.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodes.getParentNode();
					if (element.getAttribute("id").toString().compareTo("Dark") == 0) {
						Element e1 = (Element) nodes;
						int r = Integer.parseInt(e1
								.getElementsByTagName("rectangle").item(0)
								.getTextContent());
						int p = Integer.parseInt(e1
								.getElementsByTagName("position").item(0)
								.getTextContent());
						Game.stones[r][p] = Game.BLACK;
					} else if (element.getAttribute("id").toString()
							.compareTo("Light") == 0) {
						Element e1 = (Element) nodes;
						int r = Integer.parseInt(e1
								.getElementsByTagName("rectangle").item(0)
								.getTextContent());
						int p = Integer.parseInt(e1
								.getElementsByTagName("position").item(0)
								.getTextContent());
						Game.stones[r][p] = Game.WHITE;
					}
				}
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					if (Game.stones[i][j] == Game.WHITE) {

					}
				}
			}
			Game.statusController.refresh(Game.getLight(), Game.getDark());
			Game.gameController.mouse(Game.stones);

			if (Game.isPlayer()) {
				switch (Game.currentState) {
				case TAKE:
					Game.statusController.statusUpdate(Game.getLight()
							.getName() + " is milling");
					break;
				case MOVE:
					Game.statusController.statusUpdate(Game.getLight()
							.getName() + " to move");
					break;
				case PLACE:
					Game.statusController.statusUpdate(Game.getLight()
							.getName() + " to place");
					break;
				}
			} else {
				switch (Game.currentState) {
				case TAKE:
					Game.statusController.statusUpdate(Game.getDark().getName()
							+ " is milling");
					break;
				case MOVE:
					Game.statusController.statusUpdate(Game.getDark().getName()
							+ " to move");
					break;
				case PLACE:
					Game.statusController.statusUpdate(Game.getDark().getName()
							+ " to place");
					break;
				}
			}

			logger.info("Xml file was read properly!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to read the xml file");
		}
	}

	/**
	 * Get the name of the input file.
	 * 
	 * @return the name of the input file.
	 */
	public static String getFileName() {
		return fileName;
	}

	/**
	 * Set the name of the input file.
	 * 
	 * @param fileName
	 *            is the name of the input file.
	 * 
	 */
	public static void setFileName(String fileName) {
		LoadXml.fileName = fileName;
	}
}
