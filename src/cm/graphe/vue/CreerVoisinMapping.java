package cm.graphe.vue;

import java.util.ArrayList;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
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
 * <b>Class CreerVoisinMapping</b><br><br>
 * 
 * Cette classe permet de créer un noeud voisin pour un noeud.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Noeud
 * @see Graphe
 * 
 * @version 1.0
 */
public class CreerVoisinMapping {
	private Stage stageDialogue;

	@FXML
	private VBox vbox;
	protected ArrayList<CheckBox>  voisins = new ArrayList<CheckBox>();
	protected ArrayList<TextField>  poids = new ArrayList<TextField>();
	
	private MainClass main;	
	private Graphe g = null;
	private Noeud noeud;
	
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
	public void setMainClass(MainClass m, Graphe g) {
		main = m;
		this.g = g;
		GridPane grille = new GridPane();
		
		if(g == null) {
			for(Noeud noeu : main.getListDeNoeud()) {
				if(!noeud.getLabel().get().equals(noeu.getLabel().get())) {
					CheckBox check = new CheckBox(noeu.getLabel().get());
					if(noeud.estVoisin(noeu) != -1)
						check.setSelected(true);
					voisins.add(check);
					TextField tex = new TextField(String.valueOf(noeud.getPoidsUnSucesseur(noeu.getId())));
					if(noeud.estVoisin(noeu) == -1)
						tex.setText("1");
					poids.add(tex);
				}
			}
		
			int i = 0;
			for(CheckBox che : voisins) {
				HBox hbox = new HBox();
				hbox.getChildren().add(che);
				if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O || main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_O) {
					hbox.getChildren().add(new Label("       Poids     "));
					hbox.getChildren().add(poids.get(voisins.indexOf(che)));
				}
				hbox.setAlignment(Pos.CENTER);
				grille.add(hbox, 0, i);
				i++;
			}
		} else {
			for(Noeud noeu : g.getListeNoeud()) {
				if(!noeud.getLabel().get().equals(noeu.getLabel().get())) {
					CheckBox check = new CheckBox(noeu.getLabel().get());
					for(Noeud n : noeud.getSuccesseurs()) {
						if(n.getLabel().get().equals(noeu.getLabel().get()))
							check.setSelected(true);
					}
					voisins.add(check);
					TextField tex = new TextField(String.valueOf(noeud.getPoidsUnSucesseur(noeu.getId())));
					if(noeud.estVoisin(noeu) == -1)
						tex.setText("1");
					poids.add(tex);
				}
			}
		
			int i = 0;
			for(CheckBox che : voisins) {
				HBox hbox = new HBox();
				hbox.getChildren().add(che);
				hbox.setAlignment(Pos.CENTER);
				grille.add(hbox, 0, i);
				i++;
			}
		}
		
		vbox.getChildren().add(grille);
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
	public void setNoeud(Noeud n) {
		noeud = n;
	}
	
	/**
	 * <b>Class controlerFormulaire</b><br><br>
	 * 
	 * Méthode de contrôle de la validité des données saisies.<br>
	 * 
	 * @param n Noeud
	 */
	private boolean controlerFormulaire() {
		if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O || main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_O) {
			boolean isOk = true;	
			
			for(TextField p : poids) {
				if(voisins.get(poids.indexOf(p)).isSelected()) {
					if(p.getText().isEmpty() || Integer.parseInt(p.getText().trim()) == 0) {
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
	
	/**
	 * <b>Class valider</b><br><br>
	 * 
	 * Cette méthode permet de sauvegarder le graphe.<br>
	 */
	public void valider() {
		if(controlerFormulaire()) {
			if(g == null) {
				for(CheckBox che : voisins) {
					Noeud neud = main.getGraphe().getNoeud(che.getText());
					if(che.isSelected()) {
						noeud.ajouteVoisin(neud, Integer.parseInt(poids.get(voisins.indexOf(che)).getText()));
						if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O || main.getGraphe().getTypeGraphe() == TypeGraphe.SIMPLE_N_O)
							neud.ajouteVoisin(noeud, Integer.parseInt(poids.get(voisins.indexOf(che)).getText()));
					}
					else {
						if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O || main.getGraphe().getTypeGraphe() == TypeGraphe.SIMPLE_N_O) {
							noeud.enleveVoisin(neud);
							neud.enleveVoisin(noeud);
						}	
					}
					main.getGraphe().getListeNoeud().set(main.getGraphe().getNoeudId(noeud.getId()), noeud);
					main.getGraphe().getListeNoeud().set(main.getGraphe().getNoeudId(neud.getId()), neud);
				}
				
				//On ferme la boîte de dialogue
				stageDialogue.close();
			} else {
				for(CheckBox che : voisins) {
					Noeud neud = g.getNoeud(che.getText());
					if(che.isSelected()) {
						noeud.ajouteVoisin(neud, Integer.parseInt(poids.get(voisins.indexOf(che)).getText()));
					}
					g.getListeNoeud().set(g.getNoeudId(neud.getId()), neud);
				}
				
				//On ferme la boîte de dialogue
				stageDialogue.close();
			}
			
		} else {
			Alert erreur = new Alert(AlertType.WARNING);
			erreur.setTitle("Attention ! ");
			StringBuilder sb = new StringBuilder();
			List<String> messageErreur = new ArrayList<>();
			messageErreur.add("Vous avez un poids égal à 0");
			messageErreur.stream().forEach((x) -> sb.append("\n" + x));
			erreur.setHeaderText(sb.toString());
			erreur.showAndWait();
		}
	}
}
