package cm.graphe.vue;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * <b>Class CreerNoeudMapping</b><br><br>
 * 
 * Cette classe permet de créer un Noeud dans un graphe.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Graphe
 * @see Noeud
 * 
 * @version 1.0
 */
public class CreerNoeudMapping {
	private Stage stageDialogue;
	
	@FXML
	private TextField labelFormulaire;
	
	private MainClass main;	
	private Noeud noeud;
	
	/**
	 * <b>Class setMainClass</b><br><br>
	 * 
	 * Cette méthode permet de recupérer le container principal de l'application et d'y mettre cette vue.<br>
	 * 
	 * @param m classe principale de l'application
	 * 
	 * @see MainClass
	 */
	public void setMainClass(MainClass m) {
		main = m;
		//stageDialogue = main.getStage();
	}
	
	/**
	 * <b>Class setStage</b><br><br>
	 * 
	 * Cette méthode permet de recuperer le Stage et le clore si c'est necessaire.<br>
	 * 
	 * @param s Stage
	 */
	public void setStage(Stage s) {stageDialogue = s;}
	
	public void setNoeud(Noeud n) {
		noeud = n;
		labelFormulaire.setText(n.getLabel().get());
	}
	
	/**
	 * <b>Class setMainClass</b><br><br>
	 * 
	 * Cette méthode permet de contrôler de la validité des données saisies pour créer le noeud.<br>
	 * 
	 * @return Boolean
	 */
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
	
	/**
	 * <b>Class sauvegarder</b><br><br>
	 * 
	 * Cette méthode permet de sauvegarder le noeud, que ce soit une édition ou une création.<br>
	 */
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
			stageDialogue.close();
		}
	}
}

