package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Arbre;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ArbreVueMapping {
	private Stage stageDialogue;
	Arbre arbre = new Arbre("");
	LinkedList<String> list = new LinkedList<String>();
	ArrayList<String> etat = new ArrayList<String>();
	String typeArbre;
	
	@FXML
	private ChoiceBox<String> boxVoisin = new ChoiceBox<>();
	
	@FXML
    private ImageView imageArbre;
	
	private Exporter exporter = new Exporter();
	
	private MainClass main;	
	
	public void setMainClass(MainClass m, String t) {
		main = m;
		typeArbre = t;
		int i = 0;
		for(Noeud neud : main.getListDeNoeud()) {
			boxVoisin.getItems().add(neud.getLabel().get());
			if(i == 0) {
				boxVoisin.setValue(neud.getLabel().get());
				tracerGraphe(neud.getLabel().get());
			}
			i++;
		}
		
		boxVoisin.setOnAction(e -> getChoice(boxVoisin));;
	}
	
	public void getChoice(ChoiceBox <String> choice) {
		tracerGraphe(choice.getValue());
	}
	
	protected void explorerDFS(Noeud u, Arbre a) {
		etat.set(main.getListDeNoeud().indexOf(u), "1");
		
		for(Noeud v : u.getSuccesseurs()) {
			if(etat.get(main.getListDeNoeud().indexOf(v)) == "0") {
				Arbre b = new Arbre(v.getId());
				b.setLabel(v.getLabel().get());
				a.ajouteEnfant(b);
				b.ajouteParent(a);
				explorerDFS(v, b);
			}
		}
		
		etat.set(main.getListDeNoeud().indexOf(u), "2");
	}
	
	public void tracerGraphe(String debut) {
		etat.clear();
		
		for(Noeud neu : main.getGraphe().getListeNoeud()) {
			etat.add("0");
		}
		
		Noeud neu = main.getGraphe().getNoeud(debut);
		Arbre a = new Arbre(neu.getId());
		a.setLabel(neu.getLabel().get());
    	
		if(typeArbre.equals("DFS")) { //utilise LIFO
			
			if(etat.get(main.getListDeNoeud().indexOf(neu)).equals("0")) {
				explorerDFS(neu, a);
			}
		}
		else if(typeArbre.equals("BFS")) {
			
			Queue<Noeud> fifo = new LinkedList<Noeud>();
			Map<String, Arbre> parents = new HashMap<String, Arbre>();
			parents.put(a.getId(), a);
			fifo.add(neu);
			
			while(fifo.size() > 0) {
				Noeud u = fifo.element();
				Arbre c =  parents.get(u.getId());
				for(Noeud v : u.getSuccesseurs()) {
					if(etat.get(main.getListDeNoeud().indexOf(v)) == "0") {
						etat.set(main.getListDeNoeud().indexOf(v), "1");
						Arbre b = new Arbre(v.getId());
						parents.put(b.getId(), b);
						b.setLabel(v.getLabel().get());
						b.ajouteParent(c);
						c.ajouteEnfant(b);
						fifo.add(v);
					}
				}
				fifo.remove();
				etat.set(main.getListDeNoeud().indexOf(u), "2");
			}
		}
		
		exporter.exporterArbre(a, "sauvegardeArbre");
    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
	}
	
	//On initialise ici les valeurs de la liste déroulante
	//avant de sélectionner la valeur de la personne
	public void initialize() {
		imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
	}
	
	//Afin de récupérer le stage de la popup
	//et pouvoir la clore
	public void setStage(Stage s) {stageDialogue = s;}
	
	//sauvegarde du noeud, que ce soit une édition ou une création
	public void valider() {
		
	}
}
