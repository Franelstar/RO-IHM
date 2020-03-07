/**
 * 
 */
package cm.graphe.vue;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * <b>Class CourtCheminMapping</b><br><br>
 * 
 * Cette classe permet de créer le plus court chemin entre deux noeuds.<br>
 * Cette classe implémente tous les algorithmes de création de plus court chemin.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see ArbreVueMapping
 * 
 * @version 1.0
 */
public class CourtCheminMapping {
	
	private Stage stageDialogue;
	private MainClass main;
	String typeArbre;
	
	@FXML
	private ChoiceBox<String> boxDepart = new ChoiceBox<>();
	
	@FXML
	private ChoiceBox<String> boxArrivee = new ChoiceBox<>();
	
	@FXML
    private ImageView imageArbre;
	
	@FXML
    private GridPane poidsCumule;
	
	@FXML
    private Label valeurPoidsCumule;
	
	@FXML
	private GridPane grilleParent;
	
	private Exporter exporter = new Exporter();
	
	/**
	 * <b>Class setMainClass</b><br><br>
	 * 
	 * Cette méthode permet de recupérer le container principal de l'application et d'y mettre cette vue.<br>
	 * Ici, on initialise également la liste de checkbox pour choisir le noeud de debut et de fin.<br>
	 * 
	 * @param m classe principale de l'application
	 * @param t contient le texte qui spécifi le type d'algorithme à utiliser
	 * 
	 * @see MainClass
	 */
	public void setMainClass(MainClass m, String t) {
		main = m;
		typeArbre = t;
		
		for(Noeud neud : main.getListDeNoeud()) {
			boxDepart.getItems().add(neud.getLabel().get());
			boxArrivee.getItems().add(neud.getLabel().get());
		}
		
		boxDepart.setOnAction(e -> getChoice(boxDepart, boxArrivee));
		boxArrivee.setOnAction(e -> getChoice(boxDepart, boxArrivee));
	}
	
	/**
	 * <b>Class getChoice</b><br><br>
	 * 
	 * Cette méthode modifie le chemin en tenant compte du choix de début et de fin de l'utilisateur.<br>
	 * 
	 * @param depart nom du neoud de départ
	 * @param arrivee nom du neoud d'arrivée
	 */
	public void getChoice(ChoiceBox <String> depart, ChoiceBox <String> arrivee) {
		tracerChemin(depart.getValue(), arrivee.getValue());
	}
	
	/**
	 * <b>Class tracerChemin</b><br><br>
	 * 
	 * Cette méthode permet de tracer le plus court chemin entre deux points en selectionant un algorithme.<br>
	 * 
	 * @param debut nom du neoud de départ
	 * @param fin nom du neoud d'arrivée
	 */
	protected void tracerChemin(String debut, String fin) {
		if(typeArbre.equals("DIJKSTRA")) {
			if(!debut.equals(fin))
				dijkstra(debut, fin);
			else {
				Graphe courtChemin = new Graphe("Courtchemin", TypeGraphe.SIMPLE_N_O);
				courtChemin.creerNoeud(new Noeud(debut));
				valeurPoidsCumule.setText(String.valueOf(0));
				exporter.exporterFichier(courtChemin, "sauvegardeArbre");
				imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
			}
		}
		
	}
	
	/**
	 * <b>Class dijkstra</b><br><br>
	 * 
	 * Cette méthode permet d'implementer l'algorithme de dijkstra.<br>
	 * 
	 * @param debut nom du neoud de départ
	 * @param fin nom du neoud d'arrivée
	 */
	protected void dijkstra(String debut, String fin) {
		if(debut != null && fin != null) {
			//On crée un tableau de dimension nbre de noeud
			//Une valeur est à true si on a fini avec son traitement
			HashMap<Noeud, Boolean> etat = new HashMap<Noeud, Boolean>();
			
			// Liste de noeuds qvec la dernière valeur de parent et de poids
			HashMap<Noeud,HashMap<Noeud, Integer>> liste = new HashMap<Noeud,HashMap<Noeud, Integer>>();
			
			// Liste d'attente des noeuds à traiter
			Queue<Noeud> fifo = new LinkedList<Noeud>();
			
			// Vérifier si un noeud est deja entré dans fifo
			HashMap<Noeud, Boolean> etatFifo = new HashMap<Noeud, Boolean>();
						
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				HashMap<Noeud, Integer> a = new HashMap<Noeud, Integer>();
				if(neud.getLabel().get().equals(debut)) {
					a.put(null, 0);
					liste.put(neud, a);
					etat.put(neud, true);
					fifo.add(neud);
					etatFifo.put(neud, true);
				} else {
					a.put(null, (int) Float.POSITIVE_INFINITY);
					liste.put(neud, a);
					etat.put(neud, false);
					etatFifo.put(neud, false);
				}
			}

			//On commence le traitement proprement dit
			//On fait attention avec les index des tableaux parce que c'est eux qui controle la cohérence entre les différent objet manipulé, sauf fifo
			
			while(!fifo.isEmpty()) {
				//0. On retire le dernier noeud de fifo
				Noeud dernier = fifo.remove();
				etat.remove(dernier);
				etat.put(dernier, true);
				
				//1. On cherche les voisins du dernier élément de fifo et on ajoute dans fifo s'il cela n'a pas encore été fait
				for(Noeud neud : dernier.getSuccesseurs()) {
					if(!etatFifo.get(neud)) {
						fifo.add(neud);
						etatFifo.put(neud, true);
					}
				}
				
				//2. On recalcule le poids de chaque noeud dans fifo et si c'est plus petit que celui qui est la, on met à jour
				
				for(Noeud neud : fifo) {
					if(dernier.estVoisin(neud) != -1) {
						int oldPoids = 0;
						
						int newPoids = dernier.getPoidsUnSucesseur(neud.getId());
						
						HashMap<Noeud, Integer> map = liste.get(dernier);
						Iterator<Map.Entry<Noeud,Integer>> iter = map.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry<Noeud,Integer> entry = iter.next();
							newPoids += entry.getValue();
						}
						
						map = liste.get(neud);
						iter = map.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry<Noeud,Integer> entry = iter.next();
							oldPoids = entry.getValue();
						}
						
						if(newPoids < oldPoids) {
							HashMap<Noeud, Integer> a = new HashMap<Noeud, Integer>();
							a.put(dernier, newPoids);
							liste.get(neud).clear();
							liste.put(neud, a);
						}
					}
				}
			}
			
			//3. On parcourt pour chercher le chemin le plus court qu'on met dans un graphe
			// On verifie que tous les noeuds debut et fin on été parcouru. Si c'est pas le cas, le graphe n'est pas continu
			if(etat.get(main.getGraphe().getNoeud(debut)) && etat.get(main.getGraphe().getNoeud(fin)) && !liste.get(main.getGraphe().getNoeud(fin)).containsKey(null)) {
				Graphe courtChemin = new Graphe("Courtchemin", TypeGraphe.SIMPLE_N_O);
				valeurPoidsCumule.setText(String.valueOf(creerChemin(courtChemin, main.getGraphe().getNoeud(fin), liste, 0)));
				exporter.exporterFichier(courtChemin, "sauvegardeArbre");
				imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
			}
			
		}
	}
	
	/**
	 * <b>Class creerChemin</b><br><br>
	 * 
	 * Cette méthode permet de recupérer le plus court chemin implémenté par l'algorithme de dijskra.<br>
	 * 
	 * @param g Graphe qui va contenir tous les noeuds du cheminle plus court
	 * @param n Noeud d'arrivé
	 * @param liste Liste de sortie de l'algorithme de dijskra
	 * @param p Poids cumulé
	 * 
	 * @return Integer Le poids cumulé
	 */
	protected int creerChemin(Graphe g, Noeud n, HashMap<Noeud,HashMap<Noeud, Integer>> liste, int p) {
		HashMap<Noeud, Integer> map = liste.get(n);
		Iterator<Map.Entry<Noeud,Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Noeud,Integer> entry = iter.next();
			if(entry.getKey() != null) {
				p += entry.getValue();
				creerChemin(g, entry.getKey(), liste, p);
				Noeud neu = new Noeud(n.getLabel().get());
				g.getListeNoeud().get(g.getListeNoeud().size() -1).ajouteVoisin(neu, p);
				neu.ajouteVoisin(g.getListeNoeud().get(g.getListeNoeud().size() -1), p);
				g.creerNoeud(neu);
			} else {
				Noeud neu = new Noeud(n.getLabel().get());
				g.creerNoeud(neu);
			}
		}
		return p;
	}
	
	/**
	 * <b>Class setStage</b><br><br>
	 * 
	 * Cette méthode permet de recuperer le Stage et le clore si c'est necessaire.<br>
	 * 
	 * @param s Stage
	 */
	public void setStage(Stage s) {stageDialogue = s;}

}
