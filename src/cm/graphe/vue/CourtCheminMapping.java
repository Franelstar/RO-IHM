/**
 * 
 */
package cm.graphe.vue;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Franck Anael MBIAYA
 *
 */
public class CourtCheminMapping {
	
	private Stage stageDialogue;
	private MainClass main;
	String typeArbre;
	
	@FXML
	private ChoiceBox<String> boxDepart = new ChoiceBox<>();
	
	@FXML
	private ChoiceBox<String> boxArrivee = new ChoiceBox<>();
	
	@FXML
    private ImageView imageArbre;
	
	@FXML
    private GridPane poidsCumule;
	
	@FXML
    private Label valeurPoidsCumule;
	
	@FXML
	private GridPane grilleParent;
	
	private Exporter exporter = new Exporter();
	
	public void setMainClass(MainClass m, String t) {
		main = m;
		typeArbre = t;
		
		for(Noeud neud : main.getListDeNoeud()) {
			boxDepart.getItems().add(neud.getLabel().get());
			boxArrivee.getItems().add(neud.getLabel().get());
		}
		
		boxDepart.setOnAction(e -> getChoice(boxDepart, boxArrivee));
		boxArrivee.setOnAction(e -> getChoice(boxDepart, boxArrivee));
	}
	
	public void getChoice(ChoiceBox <String> depart, ChoiceBox <String> arrivee) {
		tracerChemin(depart.getValue(), arrivee.getValue());
	}
	
	protected void tracerChemin(String debut, String fin) {
		System.out.println(debut);
		System.out.println(fin);
	}
	
	//Afin de récupérer le stage de la popup
	//et pouvoir la clore
	public void setStage(Stage s) {stageDialogue = s;}

}
