/**
 * 
 */
package cm.graphe.vue;

import cm.graphe.MainClass;
import javafx.stage.Stage;

/**
 * @author Franck Anael MBIAYA
 *
 */
public class CourtCheminMapping {
	
	private Stage stageDialogue;
	private MainClass main;
	String typeArbre;
	
	public void setMainClass(MainClass m, String t) {
		main = m;
		typeArbre = t;
	}
	
	//Afin de récupérer le stage de la popup
	//et pouvoir la clore
	public void setStage(Stage s) {stageDialogue = s;}

}
