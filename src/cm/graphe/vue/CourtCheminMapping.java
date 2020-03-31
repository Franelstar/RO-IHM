/**
 * 
 */
package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Arbre;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
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
    private Label valeurFlot;
	
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
		valeurFlot.setText("");
		valeurPoidsCumule.setText("");
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
		} else if(typeArbre.equals("BELLMAN-FORD")) {
			if(!debut.equals(fin))
				bellmanFord(debut, fin);
			else {
				Graphe courtChemin = new Graphe("Courtchemin", TypeGraphe.SIMPLE_N_O);
				courtChemin.creerNoeud(new Noeud(debut));
				valeurPoidsCumule.setText(String.valueOf(0));
				exporter.exporterFichier(courtChemin, "sauvegardeArbre");
				imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
			}
		} else if(typeArbre.equals("FORDFULKERSON")) {
			if(!debut.equals(fin)) {
				fordFulkerson(debut, fin);
			}
		}
		
	}
	
	private void fordFulkerson(String debut, String fin) {
		// TODO Auto-generated method stub
		if(debut != null && fin != null) {
			//On duplique le graphe en cour
			Graphe grapheOld = new Graphe("", TypeGraphe.PONDERE_O);
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				Noeud n = new Noeud(neud.getLabel().get());
				grapheOld.creerNoeud(n);
			}
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				for(Noeud neu : neud.getSuccesseurs()) {
					grapheOld.getNoeud(neud.getLabel().get()).ajouteVoisin(grapheOld.getNoeud(neu.getLabel().get()), neud.getPoidsUnSucesseur(neu.getId()));
				}
			}
			
			HashMap<Noeud, String> etat = new HashMap<Noeud, String>();
			for(Noeud neu : main.getGraphe().getListeNoeud()) {
				etat.put(neu, "0");
			}
			Graphe grapheResiduel = new Graphe("", TypeGraphe.PONDERE_O);
			
			while(RechercheCheminDFS(grapheResiduel, null, main.getGraphe().getNoeud(debut), main.getGraphe().getNoeud(fin), etat, false)) {
				
				//On recupere le plus petit poid
				int valeurAuglentee = (int) Float.POSITIVE_INFINITY;
				for(Noeud neud : grapheResiduel.getListeNoeud()) {
					if(!neud.getSuccesseurs().isEmpty()) {
						if(valeurAuglentee > neud.getPoidsUnSucesseur(neud.getSuccesseurs().get(0).getId())) {
							valeurAuglentee = neud.getPoidsUnSucesseur(neud.getSuccesseurs().get(0).getId());
						}
					}
				}
				
				for(Noeud neud : grapheResiduel.getListeNoeud()) {
					if(!neud.getSuccesseurs().isEmpty()) {
						String nDepart = neud.getLabel().get();
						String nArrivee = neud.getSuccesseurs().get(0).getLabel().get();
						
						Noeud nD = main.getGraphe().getNoeud(nDepart);
						Noeud nA = main.getGraphe().getNoeud(nArrivee);
						
						nD.ajouteVoisin(nA, nD.getPoidsUnSucesseur(nA.getId()) - valeurAuglentee);
						if(nA.estVoisin(nD) != -1) {
							nA.ajouteVoisin(nD, nA.getPoidsUnSucesseur(nD.getId()) + valeurAuglentee);
						} else {
							nA.ajouteVoisin(nD, valeurAuglentee);
						}
						if(nD.getPoidsUnSucesseur(nA.getId()) == 0) {
							nD.enleveVoisin(nA);
						}
					}
				}
				
				for(Noeud neu : main.getGraphe().getListeNoeud()) {
					etat.put(neu, "0");
				}
				grapheResiduel = new Graphe("", TypeGraphe.PONDERE_O);
			}
			
			int flot = 0;
			
			for(Noeud neud : main.getGraphe().getNoeud(fin).getSuccesseurs()) {
				flot += main.getGraphe().getNoeud(fin).getPoidsUnSucesseur(neud.getId());
			}
			
			valeurFlot.setText(String.valueOf(flot));
			exporter.exporterFichierOriente(main.getGraphe(), "sauvegardeArbre");
			imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
			main.setGraphe(grapheOld);
		}
	}
	
	protected Boolean RechercheCheminDFS(Graphe g, Noeud prec, Noeud debut, Noeud fin, HashMap<Noeud, String> etat, Boolean trouve) {
		etat.put(debut, "1");
		Noeud aDebut = new Noeud(debut.getLabel().get());
		if(prec != null)
			prec.ajouteVoisin(aDebut, main.getGraphe().getNoeud(prec.getLabel().get()).getPoidsUnSucesseur(debut.getId()));
		g.creerNoeud(aDebut);
		if(aDebut.getLabel().get().equals(fin.getLabel().get())) {
			trouve = true;
		} else {
			if(!debut.getSuccesseurs().isEmpty()) {
				for(Noeud v : debut.getSuccesseurs()) {
					if(etat.get(v) == "0" && !trouve) {
						trouve = RechercheCheminDFS(g, aDebut, v, fin, etat, trouve);
					}
				}
				if(!trouve) {
					if(prec != null)
						prec.enleveVoisin(aDebut);
					g.deleteNoeud(g.getNoeudId(aDebut.getId()));
				}
			} else {
				if(prec != null)
					prec.enleveVoisin(aDebut);
				g.deleteNoeud(g.getNoeudId(aDebut.getId()));
			}
		}
		
		return trouve;
    }

	/**
	 * <b>Class dijkstra</b><br><br>
	 * 
	 * Cette méthode permet d'implementer l'algorithme de dijkstra.<br>
	 * 
	 * @param debut nom du neoud de départ
	 * @param fin nom du neoud d'arrivée
	 */
	protected Graphe dijkstra(String debut, String fin) {
		if(debut != null && fin != null) {
			//On crée un tableau de dimension nbre de noeud
			//Une valeur est à true si on a fini avec son traitement
			HashMap<Noeud, Boolean> etat = new HashMap<Noeud, Boolean>();
			
			// Liste de noeuds qvec la dernière valeur de parent et de poids
			HashMap<Noeud, Double> listeDistance = new HashMap<Noeud, Double>();
			HashMap<Noeud, Noeud> listeParent = new HashMap<Noeud, Noeud>();
			Noeud noeudTraite = null;
						
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				if(neud.getLabel().get().equals(debut)) {
					etat.put(neud, true);
					listeDistance.put(neud, 0.0);
					listeParent.put(neud, null);
					noeudTraite = neud;
				} else {
					etat.put(neud, false);
					listeDistance.put(neud, Double.POSITIVE_INFINITY);
					listeParent.put(neud, null);
				}
			}
			
			for(int i = 0; i < main.getGraphe().getListeNoeud().size(); i++) {
				if(noeudTraite != null) {
					for(Noeud neud : noeudTraite.getSuccesseurs()) {
						if(!etat.get(neud)) {
							Double d = Double.valueOf(noeudTraite.getPoidsUnSucesseur(neud.getId()));
							Double d_old = Double.valueOf(listeDistance.get(neud));
							Double d_noeudTraite = Double.valueOf(listeDistance.get(noeudTraite));
							if(d_old > d + d_noeudTraite) {
								listeDistance.put(neud, d + d_noeudTraite);
								listeParent.put(neud, noeudTraite);
							}
						}
					}
					
					etat.put(noeudTraite, true);
					
					noeudTraite = null;
					Double min = Double.POSITIVE_INFINITY;
					
					for(Noeud neud : main.getGraphe().getListeNoeud()) {
						if(!etat.get(neud)) {
							if(min > Double.valueOf(listeDistance.get(neud))) {
								noeudTraite = neud;
								min = Double.valueOf(listeDistance.get(neud));
							}
						}
					}
				}
			}
			
			//3. On parcourt pour chercher le chemin le plus court qu'on met dans un graphe
			// On verifie que tous les noeuds debut et fin on été parcouru. Si c'est pas le cas, le graphe n'est pas continu
			if(etat.get(main.getGraphe().getNoeud(debut)) && etat.get(main.getGraphe().getNoeud(fin))) {
				Graphe courtChemin = new Graphe("Courtchemin", TypeGraphe.PONDERE_O);
				valeurPoidsCumule.setText(String.valueOf(creerChemin(courtChemin, main.getGraphe().getNoeud(debut), main.getGraphe().getNoeud(fin), listeDistance, listeParent, 0)));
				exporter.exporterFichierOriente(courtChemin, "sauvegardeArbre");
				imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
				return courtChemin;
			}
		}
		return null;
	}
	
	public Graphe bellmanFord(String debut, String fin) {
		if(debut != null && fin != null) {
			// Liste de noeuds qvec la dernière valeur de parent et de poids
			HashMap<Noeud, Double> liste = new HashMap<Noeud, Double>();
			HashMap<Noeud, Noeud> parent = new HashMap<Noeud, Noeud>();
			
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				if(!neud.getLabel().get().equals(debut)) {
					liste.put(neud, Double.POSITIVE_INFINITY);
					parent.put(neud, null);
				} else {
					liste.put(neud, 0.0);
					parent.put(neud, neud);
				}
			}
			
			Boolean changement = true;
			
			int k = 1;
			
			while(changement && k < main.getGraphe().getListeNoeud().size()) {
				changement = false;
				for(Noeud neud : main.getGraphe().getListeNoeud()) {
					for(Noeud neud2 : neud.getSuccesseurs()) {
						Double a1 = liste.get(neud2);
						Double a11 = liste.get(neud);
						Double a22 = Double.valueOf(neud.getPoidsUnSucesseur(neud2.getId()));
						Double a2 = a11 + a22;
						if(a1 > a2) {
							liste.put(neud2, a2);
							parent.put(neud2, neud);
							changement = true;
						}
					}
				}
				if(changement) {
					k++;
				}
			}
			
			if(changement) {
				Alert erreur = new Alert(AlertType.INFORMATION);
				erreur.setTitle("Circuit négatif trouvé ");
				StringBuilder sb = new StringBuilder();
				List<String> messageErreur = new ArrayList<>();
				messageErreur.add("Il n'est pas possible de trouver un plus court chemin");
				messageErreur.stream().forEach((x) -> sb.append("\n" + x));
				erreur.setHeaderText(sb.toString());
				erreur.showAndWait();
			} else {
				Graphe courtChemin = new Graphe("Courtchemin", TypeGraphe.PONDERE_O);
				valeurPoidsCumule.setText(String.valueOf(creerChemin(courtChemin, main.getGraphe().getNoeud(debut), main.getGraphe().getNoeud(fin), liste, parent, 0)));
				exporter.exporterFichierOriente(courtChemin, "sauvegardeArbre");
				imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
				return courtChemin;
			}
		}
		return null;
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
	protected int creerChemin(Graphe g, Noeud debut, Noeud fin, HashMap<Noeud, Double> liste, HashMap<Noeud, Noeud> parent, int p) {
		if(!debut.getLabel().get().equals(fin.getLabel().get())) {
			Noeud prec = parent.get(fin);
			creerChemin(g, debut, prec, liste, parent, p);
			
			Noeud neu = new Noeud(fin.getLabel().get());
			
			g.getListeNoeud().get(g.getListeNoeud().size() -1).ajouteVoisin(fin, main.getGraphe().getNoeud(g.getListeNoeud().get(g.getListeNoeud().size() -1).getLabel().get()).getPoidsUnSucesseur(main.getGraphe().getNoeud(fin.getLabel().get()).getId()));
			p += liste.get(fin).intValue();
			g.creerNoeud(neu);
		} else {
			Noeud neu = new Noeud(debut.getLabel().get());
			g.creerNoeud(neu);
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
