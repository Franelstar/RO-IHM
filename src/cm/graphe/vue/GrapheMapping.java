package cm.graphe.vue;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;

/**
 * @author Franck Anael MBIAYA
 *
 */

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GrapheMapping {
	@FXML
    private Accordion accordion;
	
	
	
	
	@FXML
    private TableView<Noeud> graphe;
    @FXML
    private TableColumn<Noeud, String> listeGraphe;
    @FXML
    private Label nomValeur;
    @FXML
    private Label nbreNoeudValeur;
    
    //Objet servant de référence à notre classe principale
    //afin de pouvoir récupérer la liste de nos objets.
    private MainClass main;

    //Un constructeur par défaut
    public GrapheMapping() { }

    //Méthode qui initialise notre interface graphique
    //avec nos données métier
    @FXML
    private void initialize() {
        // Initialize the graphe list.
    	nomValeur.setText("");
    	nbreNoeudValeur.setText("");
    	listeGraphe.setCellValueFactory(cellData -> cellData.getValue().getLabel());
    }

    //Méthode qui sera utilisée dans l'initialisation de l'IHM
    //dans notre classe principale
    public void setMainApp(MainClass mainApp) {
        this.main = mainApp;
        // On lie notre liste observable au composant TableView
        graphe.setItems(main.getListDeNoeud());
        nomValeur.setText(main.getGraphe().getNom().get());
        nbreNoeudValeur.setText(String.valueOf(main.getGraphe().getNbNoeuds()));
    }
}
