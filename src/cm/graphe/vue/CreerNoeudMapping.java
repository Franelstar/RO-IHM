package cm.graphe.vue;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Noeud;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Franck Anael MBIAYA
 *
 */

public class CreerNoeudMapping {
	private Stage stageDialogue;
	
	@FXML
	private TextField labelFormulaire;
	
	private MainClass main;	
	private Noeud noeud;
	
	public void setMainClass(MainClass m) {
		main = m;
		//stageDialogue = main.getStage();
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
		labelFormulaire.setText(n.getLabel().get());
	}
	
	//Méthode de contrôle de la validité des données saisies
	private boolean controlerFormulaire() {
		boolean isOk = true;
		List<String> messageErreur = new ArrayList<>();
		if (labelFormulaire.getText() == null || labelFormulaire.getText().isEmpty()) {
			isOk = false;
			messageErreur.add("Le champ \"Label\" est obligatoire");
		}
		else {
			if(main.getGraphe().contentNoeud(labelFormulaire.getText())) {
				isOk = false;
				messageErreur.add("Le label " + labelFormulaire.getText() + " existe déjà");
			}
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
	public void sauvegarder() {
		if(controlerFormulaire()) {
			noeud.setLabel(new SimpleStringProperty(labelFormulaire.getText()));

			//S'il s'agit d'une création, on ajoute la personne dans le tableau
			if(stageDialogue.getTitle().startsWith("Création")) {
				main.getGraphe().creerNoeud(noeud);
			}
			else {
				main.getGraphe().getListeNoeud().set(main.getGraphe().getNoeudId(noeud.getId()), noeud);
			}

			//On ferme la boîte de dialogue
			main.setSauver(false);
			stageDialogue.close();
		}
	}
}

