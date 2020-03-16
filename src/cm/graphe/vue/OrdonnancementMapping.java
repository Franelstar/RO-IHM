package cm.graphe.vue;

import java.io.File;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Noeud;
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
    private TableView<Noeud> graphe;
    @FXML
    private TableColumn<Noeud, String> listeGraphe;
    @FXML
    private TableColumn<Noeud, String> listenbre;
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
    	exporter.exporterFichier(main.getGraphe(), "sauvegarde");
    	imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
    	
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
       		main.afficheCreerVoisin(main.getGraphe().getNoeudIndex(index), "Créer Voisins");
       		imageVue.setImage(new Image(new File("sauvegarde.png").toURI().toString()));
       	}
   	}
    
    @FXML
    public void supprimerNoeud() {
    	int index = graphe.getSelectionModel().getSelectedIndex();
    	//Si aucune ligne n'est sélectionnée, index vaudra -1
    	if (index > -1) {
    		main.getGraphe().deleteNoeud(index);
    		main.setSauver(false);
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
