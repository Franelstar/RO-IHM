/**
 * 
 */
package cm.graphe.vue;

import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	protected ArrayList<TextField>  poids = new ArrayList<TextField>();
	
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
				poids.add(new TextField(String.valueOf(noeud.getPoidsUnSucesseur(noeu.getId()))));
			}
		}
		
		int i = 0;
		for(CheckBox che : voisins) {
			HBox hbox = new HBox();
			hbox.getChildren().add(che);
			if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
				hbox.getChildren().add(new Label("       Poids     "));
				hbox.getChildren().add(poids.get(voisins.indexOf(che)));
			}
			hbox.setAlignment(Pos.CENTER);
			grille.add(hbox, 0, i);
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
		if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			boolean isOk = true;	
			
			for(TextField p : poids) {
				if(voisins.get(poids.indexOf(p)).isSelected()) {
					if(p.getText().isEmpty() || Integer.parseInt(p.getText().trim()) <= 0) {
						isOk = false;
					}
				}
			}
			
			return isOk;
		}else {
			boolean isOk = true;	
			return isOk;
		}
	}
	
	//sauvegarde du noeud, que ce soit une édition ou une création
	public void valider() {
		if(controlerFormulaire()) {
			for(CheckBox che : voisins) {
				Noeud neud = main.getGraphe().getNoeud(che.getText());
				if(che.isSelected()) {
					noeud.ajouteVoisin(neud, Integer.parseInt(poids.get(voisins.indexOf(che)).getText()));
					neud.ajouteVoisin(noeud, Integer.parseInt(poids.get(voisins.indexOf(che)).getText()));
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
		} else {
			Alert erreur = new Alert(AlertType.WARNING);
			erreur.setTitle("Attention ! ");
			StringBuilder sb = new StringBuilder();
			List<String> messageErreur = new ArrayList<>();
			messageErreur.add("Vous avez un poids inférieur à 1");
			messageErreur.stream().forEach((x) -> sb.append("\n" + x));
			erreur.setHeaderText(sb.toString());
			erreur.showAndWait();
		}
	}
}
