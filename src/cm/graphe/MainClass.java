package cm.graphe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.Tache;
import cm.graphe.model.TypeGraphe;
import cm.graphe.vue.ArbreVueMapping;
import cm.graphe.vue.CourtCheminMapping;
import cm.graphe.vue.CreerGrapheMapping;
import cm.graphe.vue.CreerNoeudMapping;
import cm.graphe.vue.CreerPredecesseurMapping;
import cm.graphe.vue.CreerTacheMapping;
import cm.graphe.vue.CreerVoisinMapping;
import cm.graphe.vue.DiagrammeOrdonnancementMapping;
import cm.graphe.vue.GrapheMapping;
import cm.graphe.vue.MenuMapping;
import cm.graphe.vue.OrdonnancementMapping;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * <b>Class MainClass</b><br><br>
 * 
 * Cette classe permet de contrôler toute l'application.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @version 1.0
 */
public class MainClass extends Application {

	//Nous créons des variable de classes afin de pouvoir y accéder partout
	//Ceci afin de pouvoir y positionner les éléments que nous avons fait
	//Il y a un BorderPane car le conteneur principal de notre IHM
	//est un BorderPane
	private Stage stagePrincipal;
	private BorderPane conteneurPrincipal;
	private boolean sauver = true;
	private Graphe g;
	private MenuMapping controleur;
	private GrapheMapping controleurMapping = null;
	private OrdonnancementMapping ordonnancementMapping = null;
	private TypeGraphe typeGraphe = null;
	
	
	
	public MainClass() {
	}
	
	public ObservableList<Noeud> getListDeNoeud(){return g.getListeNoeud();}
	public Graphe getGraphe(){return g;}
	public void setGraphe(Graphe gra) {
		g = gra;
		initialisationContenu();
	}
	
	public void setGrapheOrdonnancement(Graphe gra) {
		g = gra;
	}
	
	public void setSauver(boolean s) {
		sauver = s;
	}
	
	public boolean getSauver() {
		return sauver;
	}
	
	public void setTypeGraphe(TypeGraphe t) {
		typeGraphe = t;
	}
	
	public TypeGraphe getTypeGraphe() {
		return typeGraphe;
	}
	
	@Override
	public void start(Stage primaryStage) {
		stagePrincipal = primaryStage;
		
		stagePrincipal.setTitle("Application de gestion des graphes");
		
		//Nous allons utiliser nos fichier FXML dans ces deux méthodes
		initialisationConteneurPrincipal();
		if(g != null)
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
			
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();
			
			stagePrincipal.setScene(scene);
			//stagePrincipal.setX(bounds.getMinX());
			//stagePrincipal.setY(bounds.getMinY());
			//stagePrincipal.setWidth(bounds.getWidth());
			//stagePrincipal.setHeight(bounds.getHeight());
			
			//Initialisation de notre contrôleur
			controleur = loader.getController();
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
			
			controleur.activeMenus();
			
			//Nous récupérons notre mappeur via l'objet FXMLLoader
			controleurMapping = loader.getController();
			//Nous lui passons notre instance de classe
			//pour qu'il puisse récupérer notre liste observable
			controleurMapping.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'un noeud et initialiser son contrôleur
	public void afficheCreerNoeud(Noeud neoud, String titre) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CreerNoeudVue.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CreerNoeudMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setNoeud(neoud);
	        controller.setMainClass(this);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'une personne et initialiser son contrôleur
	public void afficheCreerVoisin(Noeud neoud, String titre) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CreerVoisinVue.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CreerVoisinMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setNoeud(neoud);
	        
	        controller.setMainClass(this);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'un graphe et initialiser son contrôleur
	public void creerGraphe(String titre) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CreerGraphe.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CreerGrapheMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setMainClass(this);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	        
	        //On changer le mom du graphe
	        if(this.controleurMapping != null)
	        	this.controleurMapping.setNomGraphe();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'une personne et initialiser son contrôleur
	public void creerArbre(String titre, String type) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/arbreVue.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        ArbreVueMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setMainClass(this, type);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'une personne et initialiser son contrôleur
	public void creerChemin(String titre, String type) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CourtCheminVue.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CourtCheminMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setMainClass(this, type);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'un graphe et initialiser son contrôleur
	public void creerOrdonnancement(String titre) {
	    FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("vue/OrdonnancementVue.fxml"));
		try {
			//Nous récupérons notre conteneur qui contiendra les données
			//Pour rappel, c'est un AnchorPane...
			AnchorPane conteneurGraphes = (AnchorPane) loader.load();
			//Qui nous ajoutons à notre conteneur principal
			//Au centre, puisque'il s'agit d'un BorderPane
			conteneurPrincipal.setCenter(conteneurGraphes);
			
			controleur.activeMenusOrdonnancement();
			
			//Nous récupérons notre mappeur via l'objet FXMLLoader
			ordonnancementMapping = loader.getController();
			//Nous lui passons notre instance de classe
			//pour qu'il puisse récupérer notre liste observable
			ordonnancementMapping.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Méthode qui va va afficher la popup d'édition
		//ou de création d'un graphe et initialiser son contrôleur
		public void ordonnancer() {
		    if(ordonnancementMapping != null) {
		    	List<HashMap<Noeud, Double>> liste = ordonnancementMapping.ordonnancer();
		        
		        // Création d'un nouveau Stage qui sera dépendant du Stage principal
		        Stage stageDialogue = new Stage();
		        
		        //Avec cette instruction, notre fenêtre modifiée sera modale
		        //par rapport à notre stage principal
		        stageDialogue.initOwner(stagePrincipal);
		        
		        // initialisation du contrôleur
		        DiagrammeOrdonnancementMapping controller = new DiagrammeOrdonnancementMapping();
		        controller.setListe(liste);
		        controller.setTaches(ordonnancementMapping.getTache());
		        controller.start(stageDialogue);
		    }
		}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'un graphe et initialiser son contrôleur
	public void creerTache() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CreerTache.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle("Création nouvelle tâche");
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CreerTacheMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setMainClass(this);
	        controller.setMappingTache(ordonnancementMapping);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'un noeud et initialiser son contrôleur
	public void afficheModifierTache(Tache tache, String titre) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CreerTache.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CreerTacheMapping controller = loader.getController();
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	        controller.setTache(tache);
	        controller.setMainClass(this);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//Méthode qui va va afficher la popup d'édition
	//ou de création d'une personne et initialiser son contrôleur
	public void afficheCreerPredecesseurs(Tache tache, ObservableList<Tache> taches, String titre) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("vue/CreerPredecesseurVue.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Création d'un nouveau Stage qui sera dépendant du Stage principal
	        Stage stageDialogue = new Stage();
	        stageDialogue.setTitle(titre);
	        stageDialogue.initModality(Modality.WINDOW_MODAL);
	        
	        //Avec cette instruction, notre fenêtre modifiée sera modale
	        //par rapport à notre stage principal
	        stageDialogue.initOwner(stagePrincipal);
	        Scene scene = new Scene(page);
	        stageDialogue.setScene(scene);
	        
	        // initialisation du contrôleur
	        CreerPredecesseurMapping controller = loader.getController();
	        controller.setTaches(tache, taches);
	        //On passe le noeud avec laquelle nous souhaitons travailler
	        //une existante ou une nouvelle
	       // controller.setNoeud(neoud);
	        
	        controller.setMainClass(this);
	        controller.setStage(stageDialogue);
	        
	        // Show the dialog and wait until the user closes it
	        stageDialogue.showAndWait();
	        //return controller.isOkClicked();
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
