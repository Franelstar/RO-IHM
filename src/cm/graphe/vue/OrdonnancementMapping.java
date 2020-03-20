package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.Tache;
import cm.graphe.model.TypeGraphe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class OrdonnancementMapping {
	@FXML
    private Accordion accordion;
	@FXML
    private TableView<Tache> tache;
    @FXML
    private TableColumn<Tache, String> listeLabel;
    @FXML
    private TableColumn<Tache, String> listeLibelle;
    @FXML
    private TableColumn<Tache, String> listeDuree;
    @FXML
    private TableColumn<Tache, String> listePredecesseurs;
    @FXML
    private Label nomTacheValeur;
    @FXML
    private Label listePredecesseur;
    @FXML
    private Label labelListeVoisin;
    @FXML
    private Button modifierNoeud;
    @FXML
    private Button voisinNoeud;
    @FXML
    private Button supprimerNoeud;
    @FXML
    private ImageView imageVue;
    
    //Objet servant de référence à notre classe principale
    //afin de pouvoir récupérer la liste de nos objets.
    private MainClass main;
    
    private Exporter exporter = new Exporter();
    
    private ObservableList<Tache> taches = FXCollections.observableArrayList(); //Liste des noeuds
    
    public ObservableList<Tache> getTache() {
    	return taches;
    }

    //Méthode qui initialise notre interface graphique
    //avec nos données métier
    @FXML
    private void initialize() {
    	
    	taches.add(new Tache("T1", "Première tache", 3));
    	taches.add(new Tache("T2", "Deuxième tache", 7));
    	taches.add(new Tache("T3", "Troisième tache", 4));
    	taches.add(new Tache("T4", "Quatrième tache", 6));
    	taches.add(new Tache("T5", "Cinquième tache", 5));
    	taches.add(new Tache("T6", "Sixième tache", 3));
    	taches.add(new Tache("T7", "Septième tache", 2));
    	
    	
    	taches.get(2).ajouterPredecesseur(taches.get(0));
    	taches.get(3).ajouterPredecesseur(taches.get(0));
    	taches.get(3).ajouterPredecesseur(taches.get(1));
    	taches.get(4).ajouterPredecesseur(taches.get(2));
    	taches.get(5).ajouterPredecesseur(taches.get(2));
    	taches.get(5).ajouterPredecesseur(taches.get(3));
    	taches.get(6).ajouterPredecesseur(taches.get(5));
    	
    	
        // Initialize the graphe list.
    	nomTacheValeur.setText("");
    	listePredecesseur.setText("");
    	labelListeVoisin.setVisible(false);
    	modifierNoeud.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	listeLabel.setCellValueFactory(cellData -> cellData.getValue().getLabel());
    	listeLibelle.setCellValueFactory(cellData -> cellData.getValue().getLibelle());
    	listeDuree.setCellValueFactory(cellData -> cellData.getValue().getDuree().asString());
    	listePredecesseurs.setCellValueFactory(cellData -> cellData.getValue().getPredecesseursToString());
    	
    	tache.setItems(taches);
    	tache.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> initializeDescription(newValue));
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
    
    /**
	 * <b>Class initializeDescription</b><br><br>
	 * 
	 * Cette méthode permet d'initialiser la description du noeud sélectionné dans le graphe<br>
	 * 
	 * @param newValue Noeud sélectionné
	 */
    public void initializeDescription(Tache newValue) {
    	
    	//On réinitialise par défaut
    	nomTacheValeur.setText("");
    	listePredecesseur.setText("");
    	labelListeVoisin.setVisible(false);
    	modifierNoeud.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	
    	setGraphe();
    	
    	//Si un objet est passé en paramètre, on modifie l'IHM
    	if(newValue != null) {
    		//ATTENTION : les accesseurs retournent des objets Property Java FX
    		//Pour récupérer leurs vrais valeurs vous devez utiliser la méthode get()
    		nomTacheValeur.setText(newValue.getLabel().get());
    		listePredecesseur.setText(newValue.getPredecesseursToString().get());
    		labelListeVoisin.setVisible(true);
    		modifierNoeud.setVisible(true);
    		voisinNoeud.setVisible(true);
        	supprimerNoeud.setVisible(true);
    	}
    }
    
    @FXML
	public void modifierTache() {
    	int index = tache.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		main.afficheModifierTache(taches.get(index), "Modifification d'une tâche");
    		setGraphe();
    	}
	}
    
    @FXML
   	public void creerPredecesseur() {
       	int index = tache.getSelectionModel().getSelectedIndex();
       	//Si aucune ligne n'est sélectionnée, index vaudra -1
       	if (index > -1) {
       		main.afficheCreerPredecesseurs(taches.get(index), taches, "Créer Voisins");
       		setGraphe();
       	}
   	}
    
    @FXML
    public void supprimerTache() {
    	int index = tache.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		for(Tache t : taches) {
    			if(!t.getLabel().get().equals(taches.get(index).getLabel().get()))
    				t.enleverPredecesseur(taches.get(index));
    		}
    		taches.remove(index);
    		setGraphe();
    	}
    	else {
    		Alert probleme = new Alert(AlertType.ERROR);
    		probleme.setTitle("Erreur");
    		probleme.setHeaderText("Veuillez sélectionnez un noeud");
    		probleme.showAndWait();
    	}
    }
    
    public void setGraphe() {
    	Graphe graphe = new Graphe("Ordonnancement", TypeGraphe.PONDERE_O);
    	Noeud source = new Noeud("S");
    	graphe.creerNoeud(source);
    	Noeud puit = new Noeud("P");
    	graphe.creerNoeud(puit);
    	ArrayList<Noeud> neuds = new ArrayList<Noeud>();
    	
    	for(Tache tache : taches) {
    		Noeud neud = new Noeud(tache.getLabel().get());
    		neuds.add(neud);
    		graphe.creerNoeud(neud);
    	}
    	
    	int i = 0;
    	for(Tache tache : taches) {
    		if(tache.getPredecesseurs().isEmpty()) {
    			source.ajouteVoisin(neuds.get(i), 0);
    		} else {
    			for(Tache tacheP : tache.getPredecesseurs()) {
    				for(Noeud neu : neuds) {
    					if(neu.getLabel().get().equals(tacheP.getLabel().get())) {
    						neu.ajouteVoisin(neuds.get(i), tacheP.getDuree().get());
    					}
    				}
    			}
    		}
    		i++;
    	}
    	
    	for(Noeud neu : neuds) {
    		if(neu.getNbVoisins() == 0) {
    			for(Tache tache : taches) {
    				if(tache.getLabel().get().equals(neu.getLabel().get())) {
    					neu.ajouteVoisin(puit, tache.getDuree().get());
    				}
    			}
    		}
    	}
    	
    	main.setGrapheOrdonnancement(graphe);
    	
    	exporter.exporterFichierOriente(main.getGraphe(), "sauvegarde");
    	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    }

    public List<HashMap<Noeud, Double>>  ordonnancer(){
    	if(!taches.isEmpty()) {
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
}
