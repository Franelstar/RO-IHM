package cm.graphe.vue;

import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * @author Franck Anael MBIAYA
 *
 */

public class CreerGrapheMapping {
	private Stage stageDialogue;
	
	@FXML
	TextField creerGrapheFormulaire;
	
	private MainClass main;	
	
	public void setMainClass(MainClass m) {
		main = m;
	}
	
	//On initialise ici les valeurs de la liste déroulante
	//avant de sélectionner la valeur de la personne
	public void initialize() {
		
	}
	
	//Afin de récupérer le stage de la popup
	//et pouvoir la clore
	public void setStage(Stage s) {stageDialogue = s;}
	
	//Méthode de contrôle de la validité des données saisies
	private boolean controlerFormulaire() {
		boolean isOk = true;
		List<String> messageErreur = new ArrayList<>();
		if (creerGrapheFormulaire.getText() == null || creerGrapheFormulaire.getText().isEmpty()) {
			isOk = false;
			messageErreur.add("Le champ \"Label\" est obligatoire");
		}
		
		if(!isOk) {
			Alert erreur = new Alert(AlertType.ERROR);
			erreur.setTitle("Erreur ! ");
			StringBuilder sb = new StringBuilder();
			messageErreur.stream().forEach((x) -> sb.append("\n" + x));
			erreur.setHeaderText(sb.toString());
			erreur.showAndWait();
		}	
		return isOk;
	}
	
	@FXML
	public void annuler() {
		//On ferme la boîte de dialogue
		stageDialogue.close();
	}
	
	//sauvegarde du noeud, que ce soit une édition ou une création
	public void valider() {
		if(controlerFormulaire()) {
			if(main.getSauver()) {
				Graphe gra = new Graphe("");
				gra.setNom(new SimpleStringProperty(creerGrapheFormulaire.getText()));
				main.setGraphe(gra);
				
				//On ferme la boîte de dialogue
				stageDialogue.close();
			}
			else {
				Alert erreur = new Alert(AlertType.CONFIRMATION);
				erreur.setTitle("Attention ! ");
				StringBuilder sb = new StringBuilder();
				List<String> messageErreur = new ArrayList<>();
				messageErreur.add("Voulez vous continuer sans sauvegarder le graphe en cour ?");
				messageErreur.stream().forEach((x) -> sb.append("\n" + x));
				erreur.setHeaderText(sb.toString());
				erreur.showAndWait();
				
				if (erreur.getResult() == ButtonType.OK) {
					main.getGraphe().setNom(new SimpleStringProperty(creerGrapheFormulaire.getText()));
					main.getListDeNoeud().clear();
				}
				
				//On ferme la boîte de dialogue
				stageDialogue.close();
			}
		}
	}
}
