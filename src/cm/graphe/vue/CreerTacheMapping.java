package cm.graphe.vue;

import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.Tache;
import cm.graphe.model.TypeGraphe;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CreerTacheMapping {
private Stage stageDialogue;
	
	@FXML
	private TextField labelFormulaire;
	@FXML
	private TextField libelleFormulaire;
	@FXML
	private TextField dureeFormulaire;
	
	private MainClass main;	
	private Tache tache;
	private ObservableList<Tache> taches = FXCollections.observableArrayList();
	OrdonnancementMapping oTaches;
	
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
	
	public void setTache(Tache t) {
		tache = t;
		labelFormulaire.setEditable(false);
		labelFormulaire.setText(t.getLabel().get());
		libelleFormulaire.setText(t.getLibelle().get());
		dureeFormulaire.setText(String.valueOf(t.getDuree().get()));
	}
	public void setMappingTache(OrdonnancementMapping oT) {
		oTaches = oT;
		taches = oTaches.getTache();
		
		labelFormulaire.setEditable(false);
		int index = Tache.instanceCount+1;
		labelFormulaire.setText("T" + index);
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
		} else if (libelleFormulaire.getText() == null || libelleFormulaire.getText().isEmpty()) {
			isOk = false;
			messageErreur.add("Le champ \"Libelle\" est obligatoire");
		} else if (dureeFormulaire.getText() == null || dureeFormulaire.getText().isEmpty()) {
			isOk = false;
			messageErreur.add("Le champ \"Durée\" est obligatoire");
		}
		
		try {
	        float myFloat = Float.parseFloat(dureeFormulaire.getText());
	        
	      } catch (NumberFormatException ex) {
	    	  isOk = false;
			messageErreur.add("Le champ \"Durée\" est un nombre entier");
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

			//S'il s'agit d'une création
			if(stageDialogue.getTitle().startsWith("Création")) {
				//main.getGraphe().creerNoeud(noeud);
				Tache t = new Tache(labelFormulaire.getText(), libelleFormulaire.getText(), Integer.parseInt(dureeFormulaire.getText()));
				taches.add(t);
				//oTaches.getTache();
			}
			else {
				tache.setLabel(labelFormulaire.getText());
				tache.setLibelle(libelleFormulaire.getText());
				tache.setDuree(Integer.parseInt(dureeFormulaire.getText()));
			}

			//On ferme la boîte de dialogue
			stageDialogue.close();
		}
	}
}
