/**
 * 
 */
package cm.graphe.vue;

import java.util.ArrayList;
import cm.graphe.MainClass;
import cm.graphe.model.Noeud;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 * @author Franck Anael MBIAYA
 *
 */
public class CreerVoisinMapping {
	private Stage stageDialogue;
	
	//@FXML
	//private ChoiceBox<String> boxVoisin = new ChoiceBox<>(FXCollections.observableArrayList("Asparagus", "Beans", "Broccoli", "Cabbage" , "Carrot", "Celery", "Cucumber", "Leek", "Mushroom" , "Pepper", "Radish", "Shallot", "Spinach", "Swede" , "Turnip"));
 
	@FXML
	private VBox vbox;
	protected ArrayList<CheckBox>  voisins = new ArrayList<CheckBox>();
	
	private MainClass main;	
	private Noeud noeud;
	
	public void setMainClass(MainClass m) {
		main = m;
		
		GridPane grille = new GridPane();
		
		for(Noeud noeu : main.getListDeNoeud()) {
			if(!noeud.getLabel().get().equals(noeu.getLabel().get())) {
				CheckBox check = new CheckBox(noeu.getLabel().get());
				if(noeud.estVoisin(noeu) != -1)
					check.setSelected(true);
				voisins.add(check);
			}
		}
		
		int i = 0;
		for(CheckBox che : voisins) {
			che.setAlignment(Pos.CENTER);
			grille.add(che, 0, i);
			i++;
		}
		
		vbox.getChildren().add(grille);
	}
	
	//On initialise ici les valeurs de la liste déroulante
	//avant de sélectionner la valeur de la personne
	public void initialize() {
		
	}
	
	//Afin de récupérer le stage de la popup
	//et pouvoir la clore
	public void setStage(Stage s) {stageDialogue = s;}
	
	public void setNoeud(Noeud n) {
		noeud = n;
	}
	
	//Méthode de contrôle de la validité des données saisies
	private boolean controlerFormulaire() {
		boolean isOk = true;	
		return isOk;
	}
	
	//sauvegarde du noeud, que ce soit une édition ou une création
	public void valider() {
		if(controlerFormulaire()) {
			for(CheckBox che : voisins) {
				Noeud neud = main.getGraphe().getNoeud(che.getText());
				if(che.isSelected()) {
					noeud.ajouteVoisin(neud, 1);
					neud.ajouteVoisin(noeud, 1);
				}
				else {
					noeud.enleveVoisin(neud);
					neud.enleveVoisin(noeud);
				}
				main.getGraphe().getListeNoeud().set(main.getGraphe().getNoeudId(noeud.getId()), noeud);
				main.getGraphe().getListeNoeud().set(main.getGraphe().getNoeudId(neud.getId()), neud);
			}
			
			//On ferme la boîte de dialogue
			main.setSauver(false);
			stageDialogue.close();
		}
	}
}
