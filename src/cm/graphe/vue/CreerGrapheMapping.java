package cm.graphe.vue;

import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
import cm.graphe.model.TypeGraphe;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * <b>Class CreerGrapheMapping</b><br><br>
 * 
 * Cette classe permet de créer un graphe.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Graphe
 * 
 * @version 1.0
 */
public class CreerGrapheMapping {
	private Stage stageDialogue;
	
	@FXML
	TextField creerGrapheFormulaire;
	
	private MainClass main;	
	
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
	}
	
	/**
	 * <b>Class setStage</b><br><br>
	 * 
	 * Cette méthode permet de recuperer le Stage et le clore si c'est necessaire.<br>
	 * 
	 * @param s Stage
	 */
	public void setStage(Stage s) {stageDialogue = s;}
	
	/**
	 * <b>Class controlerFormulaire</b><br><br>
	 * 
	 * Cette méthode permet de vérifier que le formulaire de création d'un graphe est correct.<br>
	 * 
	 * @return Boolean 
	 */
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
	
	/**
	 * <b>Class valider</b><br><br>
	 * 
	 * Cette méthode permet de sauvegarder le graphe.<br>
	 */
	public void valider() {
		TypeGraphe t_g = main.getTypeGraphe();
		Graphe gra = new Graphe("", t_g);
		
		gra.setNom(new SimpleStringProperty(creerGrapheFormulaire.getText()));
		main.setGraphe(gra);
		
		//On ferme la boîte de dialogue
		stageDialogue.close();
	}
}
