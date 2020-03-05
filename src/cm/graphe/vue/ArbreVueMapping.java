package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.stream.Collectors;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Arbre;
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
 * Cette classe permet de construire tous les <b>arbres</b> des différents graphes.
 * Mathématiquement parlant, un nombre rationnel est constitué d'un numérateur et d'un dénominateur.
 * 
 * @author Franck Anael MBIAYA
 * 
 * @since 1.0
 * 
 * @version 1
 */

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
	
	@FXML
	private GridPane grilleParent;
	
	private Exporter exporter = new Exporter();
	
	private MainClass main;	
	
	/**
	 * Cette méthode permet de recupérer le container principal de l'application et de mettre cette .
	 * Mathématiquement parlant, un nombre rationnel est constitué d'un numérateur et d'un dénominateur.
	 * 
	 * @param m classe principale de l'application
	 * @param t contient le texte qui spécifi le type d'arbre à construire
	 * 
	 * @return Ne retourne rien
	 */
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
	
	public void tracerGraphe(String debut) {
		etat.clear();
		grilleParent.setDisable(false);
		
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
			
			exporter.exporterArbre(a, "sauvegardeArbre");
	    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
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
			
			exporter.exporterArbre(a, "sauvegardeArbre");
	    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
		} else if(typeArbre.equals("PRIM") && main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			LinkedList<Arbre> table = new LinkedList<Arbre>();
			table.add(a);
			
			valeurPoidsCumule.setText(explorerPrimMin(table));
			exporter.exporterArbre(a, "sauvegardeArbre");
	    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
		} else if(typeArbre.equals("PRIM_MAX") && main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			LinkedList<Arbre> table = new LinkedList<Arbre>();
			table.add(a);
			
			valeurPoidsCumule.setText(explorerPrimMax(table));
			exporter.exporterArbre(a, "sauvegardeArbre");
	    	imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
		} else if(typeArbre.equals("KRUSKAL") && main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			grilleParent.setDisable(true);
			explorerKruskal();
		} else if(typeArbre.equals("KRUSKAL_MAX") && main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
			grilleParent.setDisable(true);
			explorerKruskalMax();
		}
	}
	
	/**
	 * Cette méthode permet de tracer l'arbre couvrant de poids minimal d'un graphe connexe pondéré avec l'algorithme de prim.
	 * 
	 * On parcourt les différents noeuds à partir d'un noeud de départ, tout en gardant le voisin dont l'arrete a le poids minimal
	 * 
	 * @param table contient l'arbre correspondant au noeud de départ
	 * 
	 * @return void
	 */
	protected String explorerPrimMin(LinkedList<Arbre> table) {
		Map<String, Integer> liste = new HashMap<String, Integer>(); //Pour stocker les differents sommet et leur poids
		int poidsCumul = 0; //Pour calculer le poids total de l'arbre
		
		for(int i = 0; i < main.getGraphe().getListeNoeud().size(); i++) { //chaque passage créera un noeud dans l'arbre
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
				    	//Si les deux sommets de cet arrete ont deja été utilisé
						if(part1.equals(ar.getId()) || part2.equals(ar.getId())) {
							if(v)
								iter.remove();
							v = true;
						}
					}
				}
				
				//On recupere la plus petite arrete
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
	
	/**
	 * Cette méthode permet de tracer l'arbre couvrant de poids maximal d'un graphe connexe pondéré avec l'algorithme de prim.
	 * 
	 * On parcourt les différents noeuds à partir d'un noeud de départ, tout en gardant le voisin dont l'arrete a le poids maximal
	 * 
	 * @param table contient l'arbre correspondant au noeud de départ
	 * 
	 * @return void
	 */
	protected String explorerPrimMax(LinkedList<Arbre> table) {
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
							if(liste.get(key) > liste.get(verifPlusPetit)) {
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
	
	/**
	 * Cette méthode permet de tracer l'arbre couvrant de poids minimal d'un graphe connexe pondéré avec l'algorithme de kruskal.
	 * 
	 * @param table contient l'arbre correspondant au noeud de départ
	 * 
	 * @return void
	 */
	protected void explorerKruskal() {
		Map<String, Integer> liste = new HashMap<String, Integer>();
		int poidsCumul = 0;
		
		//On recupere tous les arretes
		for(Noeud neud : main.getGraphe().getListeNoeud()) {
			for(Noeud neudS : neud.getSuccesseurs()) {
				String key = neudS.getLabel().get() + "@-@" + neud.getLabel().get();
				String key2 = neud.getLabel().get() + "@-@" + neudS.getLabel().get();
				if(!liste.containsKey(key) && !liste.containsKey(key2)) {
					liste.put(key, neud.getPoidsUnSucesseur(neudS.getId()));
				}
			}
		}
		
		//ensuite on classe les noeuds dans liste par ordre croissant
		Comparator<Entry<String, Integer>> valueComparator = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
		
		Map<String, Integer> sortedMap = 
				liste.entrySet().stream().
			    sorted(valueComparator).
			    collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                             (e1, e2) -> e1, LinkedHashMap::new));	    
		
		// On crée un graphe qui sera l'arbre qu'on recherche
		Graphe g = new Graphe("", TypeGraphe.SIMPLE_N_O);
		// on met tous les noeuds dans le nouveau graphe
		for(Noeud neud : main.getGraphe().getListeNoeud()) {
			g.creerNoeud(new Noeud(neud.getLabel().get()));
		}
		
		// Maintenant on parcour les arretes dans l'ordre en supprimant ceux qui ne servent plus
		for (String key : sortedMap.keySet()) {
			//On vérifie si les deux noeud de cette arrete ne sont pas encore utilisé
			String[] parts = key.split("@-@");
    		String part1 = parts[0];
    		String part2 = parts[1];
    		
			if(!kriskalCircuit(g, part1, part2, null)) {
				g.getNoeud(part1).ajouteVoisin(g.getNoeud(part2), sortedMap.get(key));
				g.getNoeud(part2).ajouteVoisin(g.getNoeud(part1), sortedMap.get(key));
				poidsCumul += sortedMap.get(key);
			}
		}
		
		valeurPoidsCumule.setText(String.valueOf(poidsCumul));
		exporter.exporterFichier(g, "sauvegardeArbre");
		imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
	}
	
	/**
	 * Cette méthode permet de tracer l'arbre couvrant de poids minimal d'un graphe connexe pondéré avec l'algorithme de kruskal.
	 * 
	 * @param table contient l'arbre correspondant au noeud de départ
	 * 
	 * @return void
	 */
	protected void explorerKruskalMax() {
		Map<String, Integer> liste = new HashMap<String, Integer>();
		int poidsCumul = 0;
		
		//On recupere tous les arretes
		for(Noeud neud : main.getGraphe().getListeNoeud()) {
			for(Noeud neudS : neud.getSuccesseurs()) {
				String key = neudS.getLabel().get() + "@-@" + neud.getLabel().get();
				String key2 = neud.getLabel().get() + "@-@" + neudS.getLabel().get();
				if(!liste.containsKey(key) && !liste.containsKey(key2)) {
					liste.put(key, neud.getPoidsUnSucesseur(neudS.getId()));
				}
			}
		}
		
		//ensuite on classe les noeuds dans liste par ordre croissant
		Comparator<Entry<String, Integer>> valueComparator = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
		
		Map<String, Integer> sortedMap = 
				liste.entrySet().stream().
			    sorted(valueComparator.reversed()).
			    collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                             (e1, e2) -> e1, LinkedHashMap::new));	    
		
		// On crée un graphe qui sera l'arbre qu'on recherche
		Graphe g = new Graphe("", TypeGraphe.SIMPLE_N_O);
		// on met tous les noeuds dans le nouveau graphe
		for(Noeud neud : main.getGraphe().getListeNoeud()) {
			g.creerNoeud(new Noeud(neud.getLabel().get()));
		}
		System.out.println(liste.toString());
		System.out.println(sortedMap.toString());
		// Maintenant on parcour les arretes dans l'ordre en supprimant ceux qui ne servent plus
		for (String key : sortedMap.keySet()) {
			//On vérifie si les deux noeud de cette arrete ne sont pas encore utilisé
			String[] parts = key.split("@-@");
    		String part1 = parts[0];
    		String part2 = parts[1];
    		
			if(!kriskalCircuit(g, part1, part2, null)) {
				int a = sortedMap.get(key);
				g.getNoeud(part1).ajouteVoisin(g.getNoeud(part2), sortedMap.get(key));
				g.getNoeud(part2).ajouteVoisin(g.getNoeud(part1), sortedMap.get(key));
				poidsCumul += sortedMap.get(key);
			}
		}
		
		valeurPoidsCumule.setText(String.valueOf(poidsCumul));
		exporter.exporterFichier(g, "sauvegardeArbre");
		imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
	}
	
	//on va de label1 vers label2
	protected Boolean kriskalCircuit(Graphe g, String label1, String label2, String prec) {
		
		if(!g.getNoeud(label1).getSuccesseurs().isEmpty()) {
			for(Noeud neu : g.getNoeud(label1).getSuccesseurs()) {
				if(neu.getLabel().get().equals(label2)) {
					return true;
				} else {
					String a = neu.getLabel().get();
					Boolean retour = false;
					if(!a.equals(prec))
						retour = kriskalCircuit(g, a, label2, label1);
					if(retour)
						return true;
				}
			}
		}
		return false;
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
