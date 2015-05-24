package mill.unideb.hu.maven;

import java.util.*;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dikder
 * Class of the game logic.
 */
public class Game implements GuiService{

	/**
	 * Logger to debug, log information and warnings.
	 */
	private static Logger logger = LoggerFactory.getLogger(Game.class);

	/**
	 * Active player.
	 */
	private static boolean player; // Light - true, Dark - false
	
	/**
	 * White stone is number 1.
	 */
	public static final int WHITE = 1;
	
	/**
	 * Black stone is number 2.
	 */
	public static final int BLACK = 2;
	
	/**
	 * Empty location is 0;
	 */
	public static final int EMPTY = 0;

	/**
	 * Where the stone is and what color it is. Stones[0] is outer rectangle, stones[1] is middle rectangle,
	 * stones[2] is inner rectangle.
	 */
	public static int[][] stones = new int[3][8]; // 0 empty, 1 light, 2 dark
	
	/**
	 * Which rectangle the stone moves to.
	 */
	private int besti;
	
	/**
	 * Which position the stone moves to.
	 */
	private int bestj;
	
	/**
	 * Player 1.
	 */
	private static Player light;
	
	/**
	 * Player 2.
	 */
	private static Player dark;
	
	/**
	 * Winner player.
	 */
	public static Player winner;
		
	/**
	 * Interface of the game controller.
	 */
	public static GameController gameController;
	
	/**
	 * Interface of the status controller.
	 */
	public static StatusController statusController;
	
	/**
	 * 
	 */
	public boolean move=false;
	
	/**
	 * Rectangle what might inner or outer or middle.
	 */
	public int fromi;
	
	/**
	 * Position of the stone.
	 */
	public int fromj;
	
	/**
	 * Rectangle what might inner or outer or middle.
	 */
	public int toi;
	
	/**
	 * Position of the stone.
	 */
	public int toj;
	
	public enum GameState {

		/** someone won. */
		WIN,
		/** time to place stones on board. */
		PLACE,
		/** time to move stones around. */
		MOVE,
		/** time to take a stone of the enemy. */
		TAKE
	}
	
	/**
	 * Actual state of the game.
	 */
	public static GameState currentState;
	
	/**
	 * Constructor of Game logic.
	 * @param light is white player.
	 * @param dark is black player.
	 */
	public Game(Player light, Player dark) {
		this.player = true;
		this.light = light;
		this.dark = dark;
		

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				stones[i][j] = EMPTY;
			}
		}
	}

	/**
	 * Start of the game.
	 */
public void play() {
		
		switch (this.getCurrentState()) {
		case PLACE:
			logger.info("Game status:PLACE");
			this.placeStone(besti, bestj);
			gameController.mouse(this.stones);
			statusController.refresh(light, dark);
			if (getCurrentState() == Game.GameState.PLACE) {
				if (isPlayer()) {
					statusController
							.statusUpdate(light.getName() + " to place");
				} else {
					statusController.statusUpdate(dark.getName() + " to place");
				}
			}
			break;
		case MOVE:
			logger.info("Game status:MOVE");
			if (this.isWin2()) {
				this.setCurrentState(GameState.WIN);
				if (this.isPlayer()) {
					this.winner = this.dark;
				} else {
					this.winner = this.light;
				}
				gameController.win(winner);
				break;
			} else {

				if (isPlayer()) {

					if (!move) {
						if (stones[besti][bestj] == Game.WHITE) {
							gameController.move(besti, bestj);
							fromi = besti;
							fromj = bestj;
							move=true;
						}
					} else {
						if(fromi==besti && fromj==bestj){
							move=false;
							gameController.mouse(stones);
							break;
						} else
						if (stones[besti][bestj] == Game.EMPTY) {
							toi = besti;
							toj = bestj;
							
							moveStone(fromi, fromj, toi, toj);
							gameController.mouse(stones);
							move=false;
						}
					}
				} else if (!move) {
					if (stones[besti][bestj] == Game.BLACK) {
						gameController.move(besti, bestj);
						fromi = besti;
						fromj = bestj;
						move=true;

					}
				} else {
					if(fromi==besti && fromj==bestj){
						move=false;
						gameController.mouse(stones);
						break;
					}else 
					if (stones[besti][bestj] == Game.EMPTY) {
						toi = besti;
						toj = bestj;
						moveStone(fromi, fromj, toi, toj);
						gameController.mouse(stones);
						move=false;
					}
				}

			}
			statusController.refresh(light, dark);
			if (getCurrentState() == Game.GameState.MOVE) {
				if (isPlayer()) {
					statusController.statusUpdate(light.getName() + " to move");
				} else {
					statusController.statusUpdate(dark.getName() + " to move");
				}
			}
			break;
		case TAKE:
			logger.info("Game status:TAKE");
			this.takeStone(besti, bestj);
			if (this.isWin()) {
				this.setCurrentState(GameState.WIN);
				if (this.isPlayer()) {
					this.winner = this.dark;
				} else {
					this.winner = this.light;
				}
				gameController.win(winner);
			}
			gameController.mouse(stones);
			statusController.refresh(light, dark);
			break;
		case WIN:
			logger.info("Game status:WIN");
			if (this.isPlayer()) {
				this.winner = this.dark;
			} else {
				this.winner = this.light;
			}
			gameController.win(winner);
			break;
		}
	}

/**
 * Examine whether there is a neighbour space which empty is.
 * @param i is rectangle what might inner or outer or middle.
 * @param j is position of the stone.
 * @return true if there is a neighbour space which empty or false if there is no one.
 */
public boolean isMoveable(int i, int j) {
		if (j % 2 == 1) {
			if (i == 0) {
				if (j != 7
						&& (stones[i][j + 1] == Game.EMPTY
								|| stones[i][j - 1] == Game.EMPTY || stones[i + 1][j] == Game.EMPTY)) {
					return true;
				} else if (j == 7
						&& (stones[i][0] == Game.EMPTY
								|| stones[i][j - 1] == Game.EMPTY || stones[i + 1][j] == Game.EMPTY)) {
					return true;
				}
			} else if (i == 1) {
				if (j != 7
						&& (stones[i][j + 1] == Game.EMPTY
								|| stones[i][j - 1] == Game.EMPTY
								|| stones[i + 1][j] == Game.EMPTY || stones[i - 1][j] == Game.EMPTY)) {
					return true;
				} else if (j == 7
						&& (stones[i][0] == Game.EMPTY
								|| stones[i][j - 1] == Game.EMPTY
								|| stones[i + 1][j] == Game.EMPTY || stones[i - 1][j] == Game.EMPTY)) {
					return true;
				}
			} else if (i == 2) {
				if (j != 7
						&& (stones[i][j + 1] == Game.EMPTY
								|| stones[i][j - 1] == Game.EMPTY || stones[i - 1][j] == Game.EMPTY)) {
					return true;
				} else if (j == 7
						&& (stones[i][0] == Game.EMPTY
								|| stones[i][j - 1] == Game.EMPTY || stones[i - 1][j] == Game.EMPTY)) {
					return true;
				}
			}
		} else if (j != 0
				&& (stones[i][j + 1] == Game.EMPTY || stones[i][j - 1] == Game.EMPTY)) {
			return true;
		} else if (j == 0
				&& (stones[i][j + 1] == Game.EMPTY || stones[i][7] == Game.EMPTY)) {
			return true;
		}

		return false;
	}

/**
 * Put the stone down.
 * @param square is rectangle what might inner or outer or middle.
 * @param j is position of the stone.
 */
public void placeStone(int square, int j) {
		if (stones[square][j] == Game.EMPTY) {
			if (this.isPlayer()) {
				if (this.light.getNumberOfStones() < 9) {
					stones[square][j] = Game.WHITE;
					this.light.raiseNumberOfStonesOnBoard();
					if (isMill(square, j)) {
						statusController.statusUpdate(light.getName()
								+ " is milling");
						setCurrentState(Game.GameState.TAKE);
						return;
					} else {
						this.setPlayer(!this.isPlayer());
					}
				}
			} else {
				if (this.dark.getNumberOfStones() < 9) {
					stones[square][j] = Game.BLACK;
					this.dark.raiseNumberOfStonesOnBoard();
					if (isMill(square, j)) {
						statusController.statusUpdate(dark.getName()
								+ " is milling");
						setCurrentState(Game.GameState.TAKE);
						return;
					} else {
						this.setPlayer(!this.isPlayer());
					}
				}
			}
		}
		if (this.dark.getNumberOfStones() == 9) {
			statusController.statusUpdate(light.getName() + " to move");
			setCurrentState(Game.GameState.MOVE);
		}
	}

/**
 * Examine that the stone is movable.
 * @param fromi is rectangle where it can be moved from what might inner or outer or middle.
 * @param fromj is position where it can be moved from.
 * @param toi is rectangle where it can be moved to what might inner or outer or middle.
 * @param toj is position where it can be moved to.
 * @return true if the stone is movable or false if it is not.
 */
public boolean acceptMove(int fromi, int fromj, int toi, int toj) {
		if (fromi == toi) {
			if ((fromj + 1) == toj || (fromj - 1) == toj
					|| (fromj == 0 && toj == 7) || (fromj == 7 && toj == 0)) {
				if (stones[toi][toj] == Game.EMPTY)
					return true;
			}

		}
		if ((fromi + 1) == toi || (fromi - 1) == toi) {
			if (fromj == toj && fromj % 2 == 1) {
				if (stones[toi][toj] == Game.EMPTY)
					return true;
			}
		}

		return false;
	}

/**
 * Move the stone from a place to another place.
 * @param fromi is rectangle where it can be moved from what might inner or outer or middle.
 * @param fromj is position where it can be moved to.
 * @param toi is rectangle where it can be moved from what might inner or outer or middle.
 * @param toj is position where it can be moved to.
 */
public void moveStone(int fromi, int fromj, int toi, int toj) {
		boolean m = true;
		if (this.isPlayer()) {
			if (this.light.getNumberOfStonesLeftToBoard() == 6) {
				m = false;
			}
		} else {
			if (this.dark.getNumberOfStonesLeftToBoard() == 6) {
				m = false;
			}
		}

		if (m) {
			if (acceptMove(fromi, fromj, toi, toj)) {
				stones[toi][toj] = stones[fromi][fromj];
				stones[fromi][fromj] = Game.EMPTY;
				if (isMill(toi, toj)) {
						statusController.statusUpdate(light.getName() + " is milling");
					this.setCurrentState(Game.GameState.TAKE);
				} else {
					this.setPlayer(!this.isPlayer());
				}
			}
		} else {
			if (stones[toi][toj] == Game.EMPTY) {
				stones[toi][toj] = stones[fromi][fromj];
				stones[fromi][fromj] = Game.EMPTY;
				if (isMill(toi, toj)) {
					statusController.statusUpdate(dark.getName() + " is milling");
					this.setCurrentState(Game.GameState.TAKE);
				} else {
					this.setPlayer(!this.isPlayer());
				}
			}
		}
	}

/**
 * Take a stone off from the board.
 * @param square is rectangle what what might inner or outer or middle.
 * @param take is position of the stone.
 */
public void takeStone(int square, int take) {

		if (this.isPlayer()) {
			if (stones[square][take] == Game.BLACK) {
				if (!inMill()) {
					if (!isMill(square, take)) {
						stones[square][take] = Game.EMPTY;
						dark.raiseNumberOfStonesLeftToBoard();
						this.setPlayer(!this.isPlayer());
						if (this.dark.getNumberOfStones() == 9) {
							this.setCurrentState(Game.GameState.MOVE);
							statusController.statusUpdate(dark.getName()
									+ " to move");
						} else {
							statusController.statusUpdate(dark.getName()
									+ " to place");
							this.setCurrentState(Game.GameState.PLACE);
						}
					}
				} else {
					stones[square][take] = Game.EMPTY;
					dark.raiseNumberOfStonesLeftToBoard();
					this.setPlayer(!this.isPlayer());
					if (this.dark.getNumberOfStones() == 9) {
						statusController.statusUpdate(light.getName()
								+ " to move");
						this.setCurrentState(Game.GameState.MOVE);
					} else {
						statusController.statusUpdate(light.getName()
								+ " to place");
						this.setCurrentState(Game.GameState.PLACE);
					}
				}
			}
		} else {
			if (stones[square][take] == Game.WHITE) {
				if (!inMill()) {
					if (!isMill(square, take)) {
						stones[square][take] = Game.EMPTY;
						light.raiseNumberOfStonesLeftToBoard();
						this.setPlayer(!this.isPlayer());
						if (this.dark.getNumberOfStones() == 9) {
							statusController.statusUpdate(light.getName()
									+ " to move");
							this.setCurrentState(Game.GameState.MOVE);
						} else {
							statusController.statusUpdate(light.getName()
									+ " to place");
							this.setCurrentState(Game.GameState.PLACE);
						}
					}
				} else {
					stones[square][take] = Game.EMPTY;
					light.raiseNumberOfStonesLeftToBoard();
					this.setPlayer(!this.isPlayer());
					if (this.dark.getNumberOfStones() == 9) {
						this.setCurrentState(Game.GameState.MOVE);
					} else {
						this.setCurrentState(Game.GameState.PLACE);
					}
				}
			}
		}

	}

/**
 * Examine whether one of the players won.
 * @return true if there is a winner or false if it is not.
 */
public boolean isWin() {
		boolean back = false;
		if (this.isPlayer()) {
			if (light.getNumberOfStonesLeftToBoard() == 7) {
				this.setCurrentState(Game.GameState.WIN);
				back = true;
			}
		} else {
			if (dark.getNumberOfStonesLeftToBoard() == 7) {
				this.setCurrentState(Game.GameState.WIN);
				back = true;
			}
		}
		return back;
	}

/**
 * Examine whether the active player can move.
 * @return true if it can't move or false if it can.
 */
public boolean isWin2() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.isPlayer()) {
					if (this.stones[i][j] == Game.WHITE) {
						if (i == 0
								&& (j == 1 || j == 3 || j == 5)
								&& (this.stones[i][j + 1] == Game.EMPTY
										|| this.stones[i][j - 1] == Game.EMPTY || this.stones[i + 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 1
								&& (j == 1 || j == 3 || j == 5)
								&& (this.stones[i][j + 1] == Game.EMPTY
										|| this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i + 1][j] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 2
								&& (j == 1 || j == 3 || j == 5)
								&& (this.stones[i][j + 1] == Game.EMPTY
										|| this.stones[i][j - 1] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						} else if ((j == 2 || j == 4 || j == 6)
								&& (this.stones[i][j + 1] == Game.EMPTY || this.stones[i][j - 1] == Game.EMPTY)) {
							return false;
						} else if (j == 0
								&& (this.stones[i][j + 1] == Game.EMPTY || this.stones[i][7] == Game.EMPTY)) {
							return false;
						} else if (i == 0
								&& j == 7
								&& (this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i][0] == Game.EMPTY || this.stones[i + 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 1
								&& j == 7
								&& (this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i][0] == Game.EMPTY
										|| this.stones[i + 1][j] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 2
								&& j == 7
								&& (this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i][0] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						}
					}
				} else {
					if (this.stones[i][j] == Game.BLACK) {
						if (i == 0
								&& (j == 1 || j == 3 || j == 5)
								&& (this.stones[i][j + 1] == Game.EMPTY
										|| this.stones[i][j - 1] == Game.EMPTY || this.stones[i + 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 1
								&& (j == 1 || j == 3 || j == 5)
								&& (this.stones[i][j + 1] == Game.EMPTY
										|| this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i + 1][j] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 2
								&& (j == 1 || j == 3 || j == 5)
								&& (this.stones[i][j + 1] == Game.EMPTY
										|| this.stones[i][j - 1] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						} else if ((j == 2 || j == 4 || j == 6)
								&& (this.stones[i][j + 1] == Game.EMPTY || this.stones[i][j - 1] == Game.EMPTY)) {
							return false;
						} else if (j == 0
								&& (this.stones[i][j + 1] == Game.EMPTY || this.stones[i][7] == Game.EMPTY)) {
							return false;
						} else if (i == 0
								&& j == 7
								&& (this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i][0] == Game.EMPTY || this.stones[i + 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 1
								&& j == 7
								&& (this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i][0] == Game.EMPTY
										|| this.stones[i + 1][j] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						} else if (i == 2
								&& j == 7
								&& (this.stones[i][j - 1] == Game.EMPTY
										|| this.stones[i][0] == Game.EMPTY || this.stones[i - 1][j] == Game.EMPTY)) {
							return false;
						}
					}
				}
			}
		}
		this.setCurrentState(Game.GameState.WIN);
		return true;
	}

/**
 * Examine whether all the stones are in mill.
 * @return true if all the stones are in mill or false if they are not.
 */
public boolean inMill() {
		boolean m = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.isPlayer()) {
					if (this.stones[i][j] == Game.BLACK) {
						if (isMill(i, j)) {
							m = true;
						} else {
							return false;
						}
					}
				} else {
					if (this.stones[i][j] == Game.WHITE) {
						if (isMill(i, j)) {
							m = true;
						} else
							return false;
					}
				}
			}
		}

		return m;
	}

/**
 * Examine whether the stone is in mill.
 * @param square is rectangle what might inner or outer or middle.
 * @param j is position of the stone.
 * @return true if it is in mill or false if it is not.
 */
public boolean isMill(int square, int j) {
		boolean ret = false;

		if (j % 2 == 1) {
			if (stones[0][j] == stones[1][j] && stones[1][j] == stones[2][j]) {
				return true;
			} else {
				if (j != 7) {
					if (stones[square][j] == stones[square][j - 1]
							&& stones[square][j] == stones[square][j + 1]) {
						return true;
					}
				} else {
					if (stones[square][j] == stones[square][j - 1]
							&& stones[square][j] == stones[square][0]) {
						return true;
					}
				}
			}
		} else {
			if (j == 2 || j == 4) {
				if ((stones[square][j] == stones[square][j + 1] && stones[square][j] == stones[square][j + 2])
						|| (stones[square][j] == stones[square][j - 1] && stones[square][j] == stones[square][j - 2])) {
					return true;
				}
			} else {
				if (j == 0) {
					if ((stones[square][j] == stones[square][j + 1] && stones[square][j] == stones[square][j + 2])
							|| (stones[square][j] == stones[square][7] && stones[square][j] == stones[square][6])) {
						return true;
					}
				} else {
					if ((stones[square][j] == stones[square][j + 1] && stones[square][j] == stones[square][0])
							|| (stones[square][j] == stones[square][j - 1] && stones[square][j] == stones[square][j - 2])) {
						return true;
					}
				}
			}
		}

		return ret;
	}

	/**
	 * Get the game actual status.
	 * @return actual status of the game.
	 */
	public GameState getCurrentState() {
		return currentState;
	}
	
	/**
	 * Set the game actual status.
	 * @param currentState is actual status of the game.
	 */
	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}
	
	/**
	 * Get who is active.
	 * @return true if light is active, false if dark is active.
	 */
	public static boolean isPlayer() {
		return player;
	}

	/**
	 * Set who is active.
	 * @param player is the active player.
	 */
	public static void setPlayer(boolean player) {
		Game.player = player;
	}
	
	/**
	 * Get the rectangle of the stone.
	 * @return rectangle what might inner or outer or middle.
	 */
	public int getBesti() {
		return besti;
	}
	
	/**
	 * Set the rectangle of the stone.
	 * @param besti is rectangle what might inner or outer or middle.
	 */
	public void setBesti(int besti) {
		this.besti = besti;
	}
	
	/**
	 * Get the position of the stone.
	 * @return position of the stone.
	 */
	public int getBestj() {
		return bestj;
	}
	
	/**
	 * Set the position of the stone.
	 * @param bestj is position of the stone.
	 */
	public void setBestj(int bestj) {
		this.bestj = bestj;
	}
	
	/**
	 * Get the light player.
	 * @return the light player.
	 */
	public static Player getLight() {
		return light;
	}

	/**
	 * Get the dark player.
	 * @return the dark player.
	 */
	public static Player getDark() {
		return dark;
	}
	
	/**
	 * Set the light player.
	 * @param light is player of the light.
	 */
	public static void setLight(Player light) {
		Game.light = light;
	}

	/**
	 * Set the dark player.
	 * @param dark is player of the dark.
	 */
	public static void setDark(Player dark) {
		Game.dark = dark;
	}
	
	/**
	 * Set the initial status.
	 */
	public void init(){
		setCurrentState(Game.GameState.PLACE);
		statusController.nameUpdate(this.light.getName(), this.dark.getName());
		statusController.statusUpdate(light.getName() + " to place");
	}
	
	/**
	 * Set the gameController and statusController.
	 */
	@Override
	public void guiservice(GUImill gui) {
		Game.gameController=gui.getDraw();
		Game.statusController=gui;
		init();
	}


}
