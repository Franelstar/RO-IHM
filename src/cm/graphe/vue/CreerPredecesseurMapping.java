package cm.graphe.vue;

import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Noeud;
import cm.graphe.model.Tache;
import cm.graphe.model.TypeGraphe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreerPredecesseurMapping {
	private Stage stageDialogue;

	@FXML
	private VBox vbox;
	protected ArrayList<CheckBox>  voisins = new ArrayList<CheckBox>();
	protected ArrayList<Tache>  preec = new ArrayList<Tache>();
	Tache tache;
	ObservableList<Tache> taches;
	
	private MainClass main;
	
	/**
	 * <b>Class setMainClass</b><br><br>
	 * 
	 * Cette méthode permet de recupérer le container principal de l'application et d'y mettre cette vue.<br>
	 * Cette méthode initialise le contenu de la fênetre de création de voisin.<br>
	 * 
	 * @param m classe principale de l'application
	 * 
	 * @see MainClass
	 */
		public void setMainClass(MainClass m) {
		main = m;
		
		for(Tache t : taches) {
			if(!t.getLabel().get().equals(tache.getLabel().get())) {
				CheckBox check = new CheckBox(t.getLabel().get());
				if(tache.estPredecesseur(t))
					check.setSelected(true);
				voisins.add(check);
				preec.add(t);
			}
		}
		
		for(CheckBox che : voisins) {
			vbox.getChildren().add(che);
		}
		
		
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
	 * <b>Class setNoeud</b><br><br>
	 * 
	 * Cette méthode permet de modifier un noeud dont on veut modifier les voisins.<br>
	 * 
	 * @param n Noeud
	 */
	public void setTaches(Tache tache, ObservableList<Tache> taches) {
		this.tache = tache;
		this.taches = taches;
	}
	
	/**
	 * <b>Class controlerFormulaire</b><br><br>
	 * 
	 * Méthode de contrôle de la validité des données saisies.<br>
	 * 
	 * @param n Noeud
	 */
	private boolean controlerFormulaire() {
		if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			boolean isOk = true;	
			
			return isOk;
		}else {
			boolean isOk = true;	
			return isOk;
		}
	}
	
	/**
	 * <b>Class valider</b><br><br>
	 * 
	 * Cette méthode permet de sauvegarder le graphe.<br>
	 */
	public void valider() {
		if(controlerFormulaire()) {
			for(int i = 0; i < voisins.size(); i++) {
				if(voisins.get(i).isSelected()) {
					preec.get(i).enleverPredecesseur(tache);
					tache.ajouterPredecesseur(preec.get(i));
				}
				else {
					tache.enleverPredecesseur(preec.get(i));
				}
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
