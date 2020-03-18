package cm.graphe.vue;

import java.io.File;

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
    	for(Tache tache : taches) {
    		Noeud neud = new Noeud(tache.getLabel().get());
    		graphe.creerNoeud(neud);
    	}
    	main.setGrapheOrdonnancement(graphe);
    	
    	exporter.exporterFichier(main.getGraphe(), "sauvegarde");
    	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    }

}
