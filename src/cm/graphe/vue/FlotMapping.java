package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FlotMapping {
	@FXML
    private Accordion accordion;
	@FXML
    private TableView<Noeud> personnes;
	@FXML
    private TableView<Noeud> taches;
    @FXML
    private TableColumn<Noeud, String> personneLabel;
    @FXML
    private TableColumn<Noeud, String> tacheLabel;
    @FXML
    private TableColumn<Noeud, String> ipersonneLabel;;
    @FXML
    private TableColumn<Noeud, String> itacheLabel;
    @FXML
    private Label nomTacheValeur;
    @FXML
    private Label listePredecesseur;
    @FXML
    private Label labelListeVoisin;
    @FXML
    private Button voisinNoeud;
    @FXML
    private Button supprimerNoeud;
    @FXML
    private Button supprimerTache;
    @FXML
    private ImageView imageVue;
    
    Graphe listePersonnes = new Graphe("personne", TypeGraphe.SIMPLE_N_O);
    Graphe listeTaches = new Graphe("personne", TypeGraphe.SIMPLE_N_O);
    
    //Objet servant de référence à notre classe principale
    //afin de pouvoir récupérer la liste de nos objets.
    private MainClass main;
    
    private Exporter exporter = new Exporter();

    //Méthode qui initialise notre interface graphique
    //avec nos données métier
    @FXML
    private void initialize() {
    	
        // Initialize the graphe list.
    	nomTacheValeur.setText("");
    	listePredecesseur.setText("");
    	labelListeVoisin.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	supprimerTache.setVisible(false);
    	
    	personneLabel.setCellValueFactory(cellData -> cellData.getValue().getLabel());
    	tacheLabel.setCellValueFactory(cellData -> cellData.getValue().getSuccesseursToString());
    	
    	ipersonneLabel.setCellValueFactory(cellData -> cellData.getValue().getLabel());
    	//itacheLabel.setCellValueFactory(cellData -> cellData.getValue().getPredecesseursToString());
    	
    	personnes.setItems(listePersonnes.getListeNoeud());
    	taches.setItems(listeTaches.getListeNoeud());
    	personnes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> initializePersonnes(newValue));
    	taches.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> initializeTaches(newValue));
    }

    /**
	 * <b>Class setMainApp</b><br><br>
	 * 
	 * Cette méthode permet de recupérer le container principal de l'application et d'y mettre cette vue.<br>
	 * Cette méthode initialise le contenu de la fênetre.<br>
	 * 
	 * @param mainApp classe principale de l'application
	 * 
	 * @see MainClass
	 */
    public void setMainApp(MainClass mainApp) {
        this.main = mainApp;
        main.setTypeGraphe(TypeGraphe.PONDERE_O);
    }
    
    public Graphe getTache() {
    	return listeTaches;
    }
    
    /**
	 * <b>Class initializeDescription</b><br><br>
	 * 
	 * Cette méthode permet d'initialiser la description du noeud sélectionné dans le graphe<br>
	 * 
	 * @param newValue Noeud sélectionné
	 */
    public void initializePersonnes(Noeud newValue) {
    	
    	//On réinitialise par défaut
    	nomTacheValeur.setText("");
    	listePredecesseur.setText("");
    	labelListeVoisin.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	supprimerTache.setVisible(false);
    	
    	setGraphe();
    	
    	//Si un objet est passé en paramètre, on modifie l'IHM
    	if(newValue != null) {
    		//ATTENTION : les accesseurs retournent des objets Property Java FX
    		//Pour récupérer leurs vrais valeurs vous devez utiliser la méthode get()
    		nomTacheValeur.setText(newValue.getLabel().get());
    		listePredecesseur.setText(newValue.getSuccesseursToString().get());
    		labelListeVoisin.setVisible(true);
    		voisinNoeud.setVisible(true);
        	supprimerNoeud.setVisible(true);
    	}
    }
    
 public void initializeTaches(Noeud newValue) {
    	
    	//On réinitialise par défaut
    	nomTacheValeur.setText("");
    	listePredecesseur.setText("");
    	labelListeVoisin.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	supprimerTache.setVisible(false);
    	
    	setGraphe();
    	
    	//Si un objet est passé en paramètre, on modifie l'IHM
    	if(newValue != null) {
    		//ATTENTION : les accesseurs retournent des objets Property Java FX
    		//Pour récupérer leurs vrais valeurs vous devez utiliser la méthode get()
    		nomTacheValeur.setText(newValue.getLabel().get());
    		supprimerTache.setVisible(true);
    	}
    }
    
    @FXML
	public void supprimerTache() {
    	int index = taches.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		for(Noeud neud : listePersonnes.getListeNoeud()) {
    			if(neud.getNbVoisins() > 0) {
	    			for(Noeud ne : neud.getSuccesseurs()) {
	    				if(ne.getLabel().get().equals(listeTaches.getNoeudIndex(index).getLabel().get())) {
	    					neud.enleveVoisin(ne);
	    				}
	    			}
    			}
    		}
    		listeTaches.deleteNoeud(index);
    		setGraphe();
    	}
    	else {
    		Alert probleme = new Alert(AlertType.ERROR);
    		probleme.setTitle("Erreur");
    		probleme.setHeaderText("Veuillez sélectionnez un noeud");
    		probleme.showAndWait();
    	}
	}
    
    @FXML
   	public void creerPredecesseur() {
    	int index = personnes.getSelectionModel().getSelectedIndex();
       	//Si aucune ligne n'est sélectionnée, index vaudra -1
       	if (index > -1) {
       		main.afficheCreerVoisin(listePersonnes.getNoeudIndex(index), "Soliciter tâche", listeTaches);
       		imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
       	}
   	}
    
    @FXML
    public void supprimerPersonne() {
    	int index = personnes.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		listePersonnes.deleteNoeud(index);
    		imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    	}
    	else {
    		Alert probleme = new Alert(AlertType.ERROR);
    		probleme.setTitle("Erreur");
    		probleme.setHeaderText("Veuillez sélectionnez un noeud");
    		probleme.showAndWait();
    	}
    }
    
    public void setGraphe() {
    	Graphe graphe = new Graphe("Flot", TypeGraphe.PONDERE_O);
    	Noeud source = new Noeud("S");
    	graphe.creerNoeud(source);
    	Noeud puit = new Noeud("P");
    	graphe.creerNoeud(puit);
    	
    	for(Noeud neud: listePersonnes.getListeNoeud()) {
    		source.ajouteVoisin(neud, 1);
    		graphe.creerNoeud(neud);
    	}
    	
    	for(Noeud neud: listeTaches.getListeNoeud()) {
    		Noeud neu = new Noeud(neud.getLabel().get());
    		for(Noeud n: listePersonnes.getListeNoeud()) {
        		if(n.estVoisin(neud) != -1) {
        			n.ajouteVoisin(neu, 1);
        			n.enleveVoisin(neud);
        		}
        	}
    		neu.ajouteVoisin(puit, 1);
    		graphe.creerNoeud(neu);
    	}
    	
    	main.setGrapheFlot(graphe);
    	
    	exporter.exporterFichierOriente(main.getGraphe(), "sauvegarde");
    	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    	
    	personnes.refresh();
    	taches.refresh();
    }

    public List<HashMap<Noeud, Double>>  ordonnancer(){
    	if(!main.getGraphe().getListeNoeud().isEmpty()) {
    		//On recherche un circuit positif
    		Boolean circuit = false;
    		setGraphe();
    		
    		if(!circuit) {
    			HashMap<Noeud, Double> MaxTot = new HashMap<Noeud, Double>();
    			HashMap<Noeud, Double> MaxTard = new HashMap<Noeud, Double>();
    			for(Noeud neud : main.getGraphe().getListeNoeud()) {
    				if(!neud.getLabel().get().equals("S")) {
    					MaxTot.put(neud, 0.0);
    				}
    				MaxTard.put(neud, 0.0);
    			}
    			
    			for(Noeud neu : main.getGraphe().getListeNoeud()) {
    				if(!neu.getLabel().get().equals("S")) {
    					RechercheMaxTot(MaxTot, main.getGraphe().getNoeud("S"), neu, 0.0);
    				}
    				RechercheMaxTard(MaxTard, neu, main.getGraphe().getNoeud("P"), 0.0, neu);
    			}
    			
    			HashMap<Noeud, Double> Debut = new HashMap<Noeud, Double>();
    			for(Noeud neud : main.getGraphe().getListeNoeud()) {
    				Debut.put(neud, MaxTard.get(main.getGraphe().getNoeud("S")) - MaxTard.get(neud));
    			}
    			
    			List<HashMap<Noeud, Double>> listOfMaps = new ArrayList<HashMap<Noeud, Double>>();
    			listOfMaps.add(Debut);
    			listOfMaps.add(MaxTot);
    			return listOfMaps;
    		}
    	}
    	return null;
    }
    
    protected void RechercheMaxTot(HashMap<Noeud, Double> tMax, Noeud debut, Noeud fin, Double max) {
    	for(Noeud neud : debut.getSuccesseurs()) {
    		max += debut.getPoidsUnSucesseur(neud.getId());
    		if(neud.getLabel().get().equals(fin.getLabel().get())) {
    			if(tMax.get(neud) < max) {
    				tMax.put(neud, max);
    			}
    		} else if(!neud.getSuccesseurs().isEmpty()) {
    			RechercheMaxTot(tMax, neud, fin, max);
    		}
    	}
    }
    
    protected void RechercheMaxTard(HashMap<Noeud, Double> tMax, Noeud debut, Noeud fin, Double max, Noeud VDebut) {
    	for(Noeud neud : debut.getSuccesseurs()) {
    		max += debut.getPoidsUnSucesseur(neud.getId());
    		if(neud.getLabel().get().equals(fin.getLabel().get())) {
    			if(tMax.get(VDebut) < max) {
    				tMax.put(VDebut, max);
    			}
    		} else if(!neud.getSuccesseurs().isEmpty()) {
    			RechercheMaxTard(tMax, neud, fin, max, VDebut);
    			max -= debut.getPoidsUnSucesseur(neud.getId());
    		}
    	}
    }
    
    @FXML
    public void creerPersonneMenu() {
    	String nom = JOptionPane.showInputDialog(null, "Entrez le nom de la personne :",
				"Création d'une personne", JOptionPane.PLAIN_MESSAGE);
    	
    	if(nom != null && !nom.isEmpty()) {
    		listePersonnes.creerNoeud(new Noeud(nom));
    		setGraphe();
    	}
    }
    
    @FXML
    public void creerTacheMenu() {
    	String nom = JOptionPane.showInputDialog(null, "Entrez le nom de la tâche :",
				"Création d'une tâche", JOptionPane.PLAIN_MESSAGE);
    	
    	if(nom != null && !nom.isEmpty()) {
    		listeTaches.creerNoeud(new Noeud(nom));
    		setGraphe();
    	}
    }
    
    @FXML
    public void sauvegarderFlot() {
    	JFileChooser fileChooser = new JFileChooser();
    	JFrame frame = new JFrame();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showSaveDialog(frame);
		frame.dispose();
		if (result == JFileChooser.APPROVE_OPTION) {
			exporter.sauverFlot(listePersonnes, listeTaches, fileChooser.getSelectedFile());
		}
    }
    
    @FXML
    public void ouvrirFlot() {
    	JFileChooser fileChooser = new JFileChooser();
    	JFrame frame = new JFrame();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(frame);
		frame.dispose();
		if (result == JFileChooser.APPROVE_OPTION) {
			exporter.lireFlot(fileChooser.getSelectedFile(), listePersonnes, listeTaches);
			setGraphe();
		}
    }
}
