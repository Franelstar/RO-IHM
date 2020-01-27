/**
 * 
 */
package cm.graphe.observer;

/**
 * @author Franck Anael MBIAYA
 *
 */
public interface Observable {
	public void addObserver(Observer obs);
	
	public void removeObserver();
	
	public void notifyObserver(String str);

}
