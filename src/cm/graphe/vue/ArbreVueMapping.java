package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
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
	Queue<String> fifo = new LinkedList<String>();
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
				for(Noeud neu : main.getGraphe().getListeNoeud()) {
					if(neu.getLabel().get().equals(neud.getLabel().get())) {
						etat.add("1");
						list.add(neud.getLabel().get());
						fifo.add(neud.getLabel().get());
					}
					else {
						etat.add("0");
					}
				}
				arbre = tracerGraphe(null);
				etat.clear();
				list.clear();
				fifo.clear();
		    	exporter.exporterArbre(arbre, "sauvegardeArbre");
		    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
			}
			i++;
		}
		
		boxVoisin.setOnAction(e -> getChoice(boxVoisin));;
	}
	
	public void getChoice(ChoiceBox <String> choice) {
		//image
		for(Noeud neu : main.getGraphe().getListeNoeud()) {
			if(neu.getLabel().get().equals(choice.getValue())) {
				etat.add("1");
				list.add(choice.getValue());
				fifo.add(choice.getValue());
			}
			else {
				etat.add("0");
			}
		}
		arbre = tracerGraphe(null);
		etat.clear();
		list.clear();
		fifo.clear();
    	exporter.exporterArbre(arbre, "sauvegardeArbre");
    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
	}
	
	public Arbre tracerGraphe(Arbre parent) {
		if(typeArbre.equals("DFS")) { //utilise LIFO
			String init = list.removeLast();
			Arbre a = new Arbre(main.getGraphe().getNoeud(init).getId());
			a.setLabel(init);
			
			//Mise à jour de list et etat
			etat.set(main.getListDeNoeud().indexOf(main.getGraphe().getNoeud(init)), "2");
			for(Noeud neu : main.getGraphe().getNoeud(init).getSuccesseurs()) {
				if(etat.get(main.getListDeNoeud().indexOf(neu)).equals("0")) {
					etat.set(main.getListDeNoeud().indexOf(neu), "1");
					list.add(neu.getLabel().get());
				}
			}
			
			//on cherche son parent
			if(parent == null) {
				a.ajouteParent(null);
			}else {
				boolean trouver = false;
				Arbre p = parent;
				while(!trouver && p != null) {
					for(Noeud neu : main.getListDeNoeud().get(main.getListDeNoeud().indexOf(main.getGraphe().getNoeud(init))).getSuccesseurs()){
						if(neu.getLabel().get().equals(p.getLabel())) {
							trouver = true;
							a.ajouteParent(p);
							p.ajouteEnfant(a);
							break;
						}
					}
					p = p.getParent();
				}
			}
			
			if(list.size() > 0) {
				tracerGraphe(a);
			}
			
			return a;
		}
		else if(typeArbre.equals("BFS")) {
			String init = fifo.remove();
			Arbre a = new Arbre(main.getGraphe().getNoeud(init).getId());
			a.setLabel(init);
			
			//Mise à jour de list et etat
			etat.set(main.getListDeNoeud().indexOf(main.getGraphe().getNoeud(init)), "2");
			for(Noeud neu : main.getGraphe().getNoeud(init).getSuccesseurs()) {
				if(etat.get(main.getListDeNoeud().indexOf(neu)).equals("0")) {
					etat.set(main.getListDeNoeud().indexOf(neu), "1");
					fifo.add(neu.getLabel().get());
				}
			}
			
			//on cherche son parent
			if(parent == null) {
				a.ajouteParent(null);
			}else {
				boolean trouver = false;
				Arbre p = parent;
				while(!trouver && p != null) {
					for(Noeud neu : main.getListDeNoeud().get(main.getListDeNoeud().indexOf(main.getGraphe().getNoeud(init))).getSuccesseurs()){
						if(neu.getLabel().get().equals(p.getLabel())) {
							trouver = true;
							a.ajouteParent(p);
							p.ajouteEnfant(a);
							break;
						}
					}
					p = p.getParent();
				}
				if(!trouver) {
					p = parent;
					while(p != null && !trouver) {						
						for(Arbre pp : p.getEnfants()) {
							for(Noeud neu : main.getListDeNoeud().get(main.getListDeNoeud().indexOf(main.getGraphe().getNoeud(init))).getSuccesseurs()){
								if(neu.getLabel().get().equals(pp.getLabel())) {
									trouver = true;
									a.ajouteParent(pp);
									pp.ajouteEnfant(a);
									break;
								}
							}
							if(trouver) {
								break;
							}
						}
						p = p.getParent();
					}
				}
			}
			
			if(fifo.size() > 0) {
				tracerGraphe(a);
			}
			
			return a;
		}
		return null;
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
