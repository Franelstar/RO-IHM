package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FordFukelsonVue {
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
	private TableView<Noeud> tableauFlot;
	@FXML
    private TableColumn<Noeud, String> personneLabel;
    @FXML
    private TableColumn<Noeud, String> tacheLabel;
    
    protected ObservableList<Noeud> flot = FXCollections.observableArrayList();
	
	private Exporter exporter = new Exporter();
	private Graphe taches;
	private Graphe grapheOld;
	
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
	public void setMainClass(MainClass m, Graphe g) {
		main = m;
		taches = g;
		
		//On duplique le graphe en cour
		if(main.getGraphe() != null && main.getGraphe().getNbNoeuds() > 0) {
			grapheOld = new Graphe("", TypeGraphe.PONDERE_O);
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				Noeud n = new Noeud(neud.getLabel().get());
				grapheOld.creerNoeud(n);
			}
			for(Noeud neud : main.getGraphe().getListeNoeud()) {
				for(Noeud neu : neud.getSuccesseurs()) {
					grapheOld.getNoeud(neud.getLabel().get()).ajouteVoisin(grapheOld.getNoeud(neu.getLabel().get()), neud.getPoidsUnSucesseur(neu.getId()));
				}
			}
			
			fordFulkerson("S", "P");
		}
	}
	
	public void fordFulkerson(String debut, String fin) {
		// TODO Auto-generated method stub
		if(debut != null && fin != null && grapheOld.getNbNoeuds() > 1) {
			
			HashMap<Noeud, String> etat = new HashMap<Noeud, String>();
			for(Noeud neu : grapheOld.getListeNoeud()) {
				etat.put(neu, "0");
			}
			Graphe grapheResiduel = new Graphe("", TypeGraphe.PONDERE_O);
			
			while(RechercheCheminBFS(grapheResiduel, null, grapheOld.getNoeud(debut), grapheOld.getNoeud(fin), etat, false)) {
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
						
						Noeud nD = grapheOld.getNoeud(nDepart);
						Noeud nA = grapheOld.getNoeud(nArrivee);
						
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
				
				for(Noeud neu : grapheOld.getListeNoeud()) {
					etat.put(neu, "0");
				}
				grapheResiduel = new Graphe("", TypeGraphe.PONDERE_O);
			}
			
			exporter.exporterFichierOriente(grapheOld, "sauvegardeArbre");
			imageArbre.setImage(new Image(new File("sauvegardeArbre.png").toURI().toString()));
			
			for(Noeud neud : taches.getListeNoeud()) {
				if(grapheOld.getNoeud(neud.getLabel().get()).getNbVoisins() == 1 && grapheOld.getNoeud(neud.getLabel().get()).getSuccesseurs().get(0).getLabel().get() != "P")
					flot.add(grapheOld.getNoeud(neud.getLabel().get()));
			}
			
			personneLabel.setCellValueFactory(cellData -> cellData.getValue().getSuccesseurs().get(0).getLabel());
	    	tacheLabel.setCellValueFactory(cellData -> cellData.getValue().getLabel());
	    	 
	    	Collections.sort(flot, (a, b) ->  a.getSuccesseurs().get(0).getLabel().get().compareTo(b.getSuccesseurs().get(0).getLabel().get()));
	    	tableauFlot.setItems(flot);
		}
	}
	
	protected Boolean RechercheCheminBFS(Graphe g, Noeud prec, Noeud debut, Noeud fin, HashMap<Noeud, String> etat, Boolean trouve) {
		etat.put(debut, "1");
		Noeud aDebut = new Noeud(debut.getLabel().get());
		if(prec != null)
			prec.ajouteVoisin(aDebut, grapheOld.getNoeud(prec.getLabel().get()).getPoidsUnSucesseur(debut.getId()));
		g.creerNoeud(aDebut);
		if(aDebut.getLabel().get().equals(fin.getLabel().get())) {
			trouve = true;
		} else {
			if(!debut.getSuccesseurs().isEmpty()) {
				for(Noeud v : debut.getSuccesseurs()) {
					if(etat.get(v) == "0" && !trouve) {
						trouve = RechercheCheminBFS(g, aDebut, v, fin, etat, trouve);
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
	 * <b>Class setStage</b><br><br>
	 * 
	 * Cette méthode permet de recuperer le Stage et le clore si c'est necessaire.<br>
	 * 
	 * @param s Stage
	 */
	public void setStage(Stage s) {stageDialogue = s;}
	
	@FXML
	public void exporter(){
		JFileChooser fileChooser = new JFileChooser();
    	JFrame frame = new JFrame();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showSaveDialog(frame);
		frame.dispose();
		if (result == JFileChooser.APPROVE_OPTION) {
			exporter.exporterFlot(flot, fileChooser.getSelectedFile());
		}
	}
}
