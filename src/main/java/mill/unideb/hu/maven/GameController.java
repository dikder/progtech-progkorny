package mill.unideb.hu.maven;

/**
 * 
 * @author dikder
 *
 */
public interface GameController {
	public void mouse(int[][] t);
	public void move(int fromi,int fromj);
	public void win(Player winner);
}
