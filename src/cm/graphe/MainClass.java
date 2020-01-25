/**
 * 
 */
package cm.graphe;

/**
 * @author Franck Anael MBIAYA
 *
 */

import java.io.IOException;

import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.vue.GrapheMapping;
import cm.graphe.vue.MenuMapping;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainClass extends Application {

	//Nous créons des variable de classes afin de pouvoir y accéder partout
	//Ceci afin de pouvoir y positionner les éléments que nous avons fait
	//Il y a un BorderPane car le conteneur principal de notre IHM
	//est un BorderPane
	private Stage stagePrincipal;
	private BorderPane conteneurPrincipal;
	private Graphe g = new Graphe("toto");
	
	private ObservableList<Noeud> listDesNoeud = FXCollections.observableArrayList();
	
	public MainClass() {
		//listDesGraphe.add(new Graphe("Titi"));
		g.creerNoeud(new Noeud("A"));
		g.creerNoeud(new Noeud("B"));
		g.creerNoeud(new Noeud("C"));
		if(g.getNbNoeuds() > 0) {
			for(Noeud noeud: g.getListeNoeud()) {
				listDesNoeud.add(noeud);
			}
		}
	}
	
	public ObservableList<Noeud> getListDeNoeud(){return listDesNoeud;}
	public Graphe getGraphe(){return g;}
	
	@Override
	public void start(Stage primaryStage) {
		stagePrincipal = primaryStage;
		
		stagePrincipal.setTitle("Application de gestion des graphes");
		
		//Nous allons utiliser nos fichier FXML dans ces deux méthodes
		initialisationConteneurPrincipal();
		initialisationContenu();
	}

	private void initialisationConteneurPrincipal() {
		//On créé un chargeur de FXML
		FXMLLoader loader = new FXMLLoader();
		//On lui spécifie le chemin relatif à notre classe
		//du fichier FXML a charger : dans le sous-dossier view
		loader.setLocation(MainClass.class.getResource("vue/menuVue.fxml"));
		try {
			//Le chargement nous donne notre conteneur
			conteneurPrincipal = (BorderPane) loader.load();
			//On définit une scène principale avec notre conteneur
			Scene scene = new Scene(conteneurPrincipal);
			//Que nous affectons à notre Stage
			stagePrincipal.setScene(scene);
			
			//Initialisation de notre contrôleur
			MenuMapping controleur = loader.getController();
			//On spécifie la classe principale afin de pour récupérer le Stage
			//Et ainsi fermer l'application
			controleur.setMainApp(this);
			
			//Pour l'afficher
			stagePrincipal.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initialisationContenu() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("vue/grapheVue.fxml"));
		try {
			//Nous récupérons notre conteneur qui contiendra les données
			//Pour rappel, c'est un AnchorPane...
			AnchorPane conteneurGraphes = (AnchorPane) loader.load();
			//Qui nous ajoutons à notre conteneur principal
			//Au centre, puisque'il s'agit d'un BorderPane
			conteneurPrincipal.setCenter(conteneurGraphes);
			
			//Nous récupérons notre mappeur via l'objet FXMLLoader
			GrapheMapping controleur = loader.getController();
			//Nous lui passons notre instance de classe
			//pour qu'il puisse récupérer notre liste observable
			controleur.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getStage() {
		// TODO Auto-generated method stub
		return this.stagePrincipal;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
