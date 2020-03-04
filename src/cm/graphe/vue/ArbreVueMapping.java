package cm.graphe.vue;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Arbre;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
	
	@FXML
    private GridPane poidsCumule;
	
	@FXML
    private Label valeurPoidsCumule;
	
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
		
		boxVoisin.setOnAction(e -> getChoice(boxVoisin));
		
		if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			poidsCumule.setVisible(true);
		} else {
			poidsCumule.setVisible(false);
		}
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
	
	protected String explorerPrim(LinkedList<Arbre> table) {
		Map<String, Integer> liste = new HashMap<String, Integer>();
		int poidsCumul = 0;
		
		for(int i = 0; i < main.getGraphe().getListeNoeud().size(); i++) {
			if(true) {
				//On stocke les voisins
				for(Noeud neud : main.getGraphe().getNoeud(table.getLast().getLabel()).getSuccesseurs()) {
					String key = table.getLast().getId() + "@-@" + neud.getId();
					String key2 = neud.getId() + "@-@" + table.getLast().getId();
					if(!liste.containsKey(key) && !liste.containsKey(key2)) {
						liste.put(key, neud.getPoidsUnSucesseur(table.getLast().getId()));
					}
				}
				
				//On recherche le voisin avec le plus petit poids
				
				String verifPlusPetit = "";
				Iterator<Map.Entry<String,Integer>> iter = liste.entrySet().iterator();
				while (iter.hasNext()) {
				    Map.Entry<String,Integer> entry = iter.next();
				    String[] parts = entry.getKey().split("@-@");
            		String part1 = parts[0];
            		String part2 = parts[1];
            		Boolean v = false;
				    for(Arbre ar : table) {
						if(part1.equals(ar.getId()) || part2.equals(ar.getId())) {
							if(v)
								iter.remove();
							v = true;
						}
					}
				}
				
				for (String key : liste.keySet()) {
					if(liste.containsKey(key)) {
						if(verifPlusPetit.equals("")) {
							verifPlusPetit = key;
						}else {
							if(liste.get(key) < liste.get(verifPlusPetit)) {
								verifPlusPetit = key;
							}
						}
					}
				}
				
				//On crée le prochain noeud
				if(!verifPlusPetit.equals("")) {
					String[] parts = verifPlusPetit.split("@-@");
	        		String part1 = parts[0];
	        		String part2 = parts[1];
	        		Boolean deja = false;
					for(Arbre ar : table) {
						if(part1.equals(ar.getId())) {
							deja = true;
						}
					}
					if(!deja) {
						Noeud neuSuivant = main.getGraphe().getNoeudIndex(main.getGraphe().getNoeudId(part1));
						Arbre aSuivant = new Arbre(neuSuivant.getId());
						aSuivant.setLabel(neuSuivant.getLabel().get());
						
						//On cherche le parent
						for(Arbre ar : table) {
							if(ar.getId().equals(part2)) {
								aSuivant.ajouteParent(ar);
								ar.ajouteEnfant(aSuivant);
							}
						}
						table.add(aSuivant);
					} else {
						Noeud neuSuivant = main.getGraphe().getNoeudIndex(main.getGraphe().getNoeudId(part2));
						Arbre aSuivant = new Arbre(neuSuivant.getId());
						aSuivant.setLabel(neuSuivant.getLabel().get());
						
						//On cherche le parent
						for(Arbre ar : table) {
							if(ar.getId().equals(part1)) {
								aSuivant.ajouteParent(ar);
								ar.ajouteEnfant(aSuivant);
							}
						}
						table.add(aSuivant);
					}
					poidsCumul += liste.get(verifPlusPetit);
					liste.remove(verifPlusPetit);
				}
			}
		}
		return String.valueOf(poidsCumul);
	}
	
	public void tracerGraphe(String debut) {
		etat.clear();
		
		for(Noeud neu : main.getGraphe().getListeNoeud()) {
			etat.add("0");
		}
		
		Noeud neu = main.getGraphe().getNoeud(debut);
		Arbre a = new Arbre(neu.getId());
		a.setLabel(neu.getLabel().get());
    	
		if(typeArbre.equals("DFS") && main.getGraphe().getTypeGraphe() == TypeGraphe.SIMPLE_N_O) { //utilise LIFO
			
			if(etat.get(main.getListDeNoeud().indexOf(neu)).equals("0")) {
				explorerDFS(neu, a);
			}
		}
		else if(typeArbre.equals("BFS") && main.getGraphe().getTypeGraphe() == TypeGraphe.SIMPLE_N_O) {
			
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
		} else if(typeArbre.equals("PRIM") && main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			LinkedList<Arbre> table = new LinkedList<Arbre>();
			table.add(a);
			
			valeurPoidsCumule.setText(explorerPrim(table));
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
