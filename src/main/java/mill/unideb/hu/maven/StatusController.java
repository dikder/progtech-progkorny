package mill.unideb.hu.maven;

/**
 * 
 * @author dikder
 *
 */
public interface StatusController {
	public void refresh(Player player1,Player player2);
	public void nameUpdate(String light,String dark);
	public void statusUpdate(String text);
}
