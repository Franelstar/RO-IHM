package cm.graphe.vue;

import java.io.File;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;

/**
 * @author Franck Anael MBIAYA
 *
 */

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

/**
 * <b>Class GrapheMapping</b><br><br>
 * 
 * Cette classe permet de manager l'interface de visualisation d'un graphe.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Graphe
 * 
 * @version 1.0
 */
public class GrapheMapping {
	@FXML
    private Accordion accordion;
	@FXML
    private TableView<Noeud> graphe;
    @FXML
    private TableColumn<Noeud, String> listeGraphe;
    @FXML
    private TableColumn<Noeud, String> listenbre;
    @FXML
    private Label nomValeur;
    @FXML
    private Label nomNoeudValeur;
    @FXML
    private Label listeVoisin;
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

    //Méthode qui initialise notre interface graphique
    //avec nos données métier
    @FXML
    private void initialize() {
        // Initialize the graphe list.
    	nomValeur.setText("");
    	nomNoeudValeur.setText("");
    	listeVoisin.setText("");
    	labelListeVoisin.setVisible(false);
    	modifierNoeud.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	listeGraphe.setCellValueFactory(cellData -> cellData.getValue().getLabel());
    	listenbre.setCellValueFactory(cellData -> cellData.getValue().getSuccesseursToString());
    	
    	graphe.getSelectionModel().selectedItemProperty().addListener(
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
        // On lie notre liste observable au composant TableView
        graphe.setItems(main.getGraphe().getListeNoeud());
        setNomGraphe();

        //image
        if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_O || main.getGraphe().getTypeGraphe() == TypeGraphe.SIMPLE_0) {
        	exporter.exporterFichierOriente(main.getGraphe(), "sauvegarde");
        	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
        } else {
        	exporter.exporterFichier(main.getGraphe(), "sauvegarde");
        	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
        }
    }
    
    /**
	 * <b>Class setNomGraphe</b><br><br>
	 * 
	 * Cette méthode permet de mêttre à jour le nom du graphe<br>
	 */
    public void setNomGraphe() {
    	String type = "";
        switch (main.getGraphe().getTypeGraphe()) {
	        case SIMPLE_N_O:
				type = " (Graphe non orienté non pondéré)";
				break;
			case PONDERE_N_O:
				type = " (Graphe non orienté pondéré)";
				break;
			case PONDERE_O:
				type = " (Graphe orienté pondéré)";
				break;
			case SIMPLE_0:
				type = " (Graphe orienté non pondéré)";
				break;
		default:
			type = " (Graphe non orienté non pondéré)";
			break;
		}
        nomValeur.setText(main.getGraphe().getNom().get() + type);
    }
    
    /**
	 * <b>Class initializeDescription</b><br><br>
	 * 
	 * Cette méthode permet d'initialiser la description du noeud sélectionné dans le graphe<br>
	 * 
	 * @param n Noeud sélectionné
	 */
    public void initializeDescription(Noeud n) {
    	//On réinitialise par défaut
    	nomNoeudValeur.setText("");
    	listeVoisin.setText("");
    	labelListeVoisin.setVisible(false);
    	modifierNoeud.setVisible(false);
    	voisinNoeud.setVisible(false);
    	supprimerNoeud.setVisible(false);
    	
    	//image
    	if(main.getGraphe().getTypeGraphe() == TypeGraphe.PONDERE_O || main.getGraphe().getTypeGraphe() == TypeGraphe.SIMPLE_0) {
        	exporter.exporterFichierOriente(main.getGraphe(), "sauvegarde");
        	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
        } else {
        	exporter.exporterFichier(main.getGraphe(), "sauvegarde");
        	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
        }
    	
    	//Si un objet est passé en paramètre, on modifie l'IHM
    	if(n != null) {
    		//ATTENTION : les accesseurs retournent des objets Property Java FX
    		//Pour récupérer leurs vrais valeurs vous devez utiliser la méthode get()
    		nomNoeudValeur.setText(n.getLabel().get());
    		listeVoisin.setText(n.getSuccesseursToString().get());
    		labelListeVoisin.setVisible(true);
    		modifierNoeud.setVisible(true);
    		voisinNoeud.setVisible(true);
        	supprimerNoeud.setVisible(true);
    	}
    }
    
    @FXML
	public void modifierNoeud() {
    	int index = graphe.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		main.afficheCreerNoeud(main.getGraphe().getNoeudIndex(index), "Modifier le Noeud");
    		imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    	}
	}
    
    @FXML
   	public void creerVoisin() {
       	int index = graphe.getSelectionModel().getSelectedIndex();
       	//Si aucune ligne n'est sélectionnée, index vaudra -1
       	if (index > -1) {
       		main.afficheCreerVoisin(main.getGraphe().getNoeudIndex(index), "Créer Voisins", null);
       		imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
       	}
   	}
    
    @FXML
    public void supprimerNoeud() {
    	int index = graphe.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		main.getGraphe().deleteNoeud(index);
    		imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    	}
    	else {
    		Alert probleme = new Alert(AlertType.ERROR);
    		probleme.setTitle("Erreur");
    		probleme.setHeaderText("Veuillez sélectionnez un noeud");
    		probleme.showAndWait();
    	}
    }

}
