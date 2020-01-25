package cm.graphe.vue;

import java.util.ArrayList;

import cm.graphe.MainClass;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	private Graphe graphe;
	private ObservableList<Noeud> listDesNoeud = FXCollections.observableArrayList();
	
	
	
	
	@FXML
    private TableView<Noeud> noeud;
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
    	//listeGraphe.setCellValueFactory(cellData -> cellData.getValue().getLabel());
    }

    //Méthode qui sera utilisée dans l'initialisation de l'IHM
    //dans notre classe principale
    public void setMainApp(MainClass mainApp) {
        this.main = mainApp;
        // On lie notre liste observable au composant TableView
        
        graphe = main.getListDeNoeud();
        nomValeur.setText(graphe.getNom().get());
        nbreNoeudValeur.setText(String.valueOf(graphe.getNbNoeuds()));
        
        //noeud.setItems(getListDeNoeud());
    }
    
    public ObservableList<Noeud> getListDeNoeud(){
    	//listDesNoeud.add(new Noeud("anael"));
    	return listDesNoeud;
    }
}
