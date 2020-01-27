/**
 * 
 */
package cm.graphe.vue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cm.graphe.MainClass;
import cm.graphe.controler.Exporter;
import cm.graphe.model.Noeud;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Franck Anael MBIAYA
 *
 */
public class MenuMapping {
	//Objet servant de référence à notre classe principale
    //afin de pouvoir récupérer le Stage principal.
	//et ainsi fermer l'application
    private MainClass main;
    
    private Exporter exporter = new Exporter();
    
    //Méthode qui sera utilisée dans l'initialisation de l'IHM
    //dans notre classe principale
    public void setMainApp(MainClass mainApp) {
        this.main = mainApp;
    }
    
	//Fermer l'application
	@FXML
	public void fermer() {
		if(main.getSauver()) {
			//Et on clos le stage principal, donc l'application
			this.main.getStage().close();
		}
		else {
			Alert erreur = new Alert(AlertType.CONFIRMATION);
			erreur.setTitle("Attention ! ");
			StringBuilder sb = new StringBuilder();
			List<String> messageErreur = new ArrayList<>();
			messageErreur.add("Voulez vous continuer sans sauvegarder le graphe en cour ?");
			messageErreur.stream().forEach((x) -> sb.append("\n" + x));
			erreur.setHeaderText(sb.toString());
			erreur.showAndWait();
			
			if (erreur.getResult() == ButtonType.OK) {
				//Et on clos le stage principal, donc l'application
				this.main.getStage().close();
			}
		}
	}
	
	@FXML
	public void nouveau() {
		this.main.afficheCreerNoeud(new Noeud(""), "Création d'un nouveau Noeud");
	}
	
    @FXML
    public void sauverGraphe() {
    	JFileChooser fileChooser = new JFileChooser();
    	JFrame frame = new JFrame();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showSaveDialog(frame);
		frame.dispose();
		if (result == JFileChooser.APPROVE_OPTION) {
			exporter.sauverGraphe(main.getGraphe(), fileChooser.getSelectedFile());
	    	main.setSauver(true);
		}
    }
    
    @FXML
    public void creerGraphe() {
    	this.main.creerGraphe("Création d'un nouveau graphe");
    	main.setSauver(false);
    }
    
    @FXML
    public void apropos() {
    	Alert erreur = new Alert(AlertType.INFORMATION);
		erreur.setTitle("A propos ");
		StringBuilder sb = new StringBuilder();
		List<String> messageErreur = new ArrayList<>();
		messageErreur.add("Créé par Franck Anael MBIAYA ?");
		messageErreur.stream().forEach((x) -> sb.append("\n" + x));
		erreur.setHeaderText(sb.toString());
		erreur.showAndWait();
    }
    
    @FXML
    public void openFileLocation() {
    	if(main.getSauver()) {
    		JFileChooser fileChooser = new JFileChooser();
	    	JFrame frame = new JFrame();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(frame);
			frame.dispose();
			if (result == JFileChooser.APPROVE_OPTION) {
				if(main.getGraphe() == null)
					main.setGraphe(exporter.lireGraphe(fileChooser.getSelectedFile()));
			}
		}
		else {
			Alert erreur = new Alert(AlertType.CONFIRMATION);
			erreur.setTitle("Attention ! ");
			StringBuilder sb = new StringBuilder();
			List<String> messageErreur = new ArrayList<>();
			messageErreur.add("Voulez vous continuer sans sauvegarder le graphe en cour ?");
			messageErreur.stream().forEach((x) -> sb.append("\n" + x));
			erreur.setHeaderText(sb.toString());
			erreur.showAndWait();
			
			if (erreur.getResult() == ButtonType.OK) {
				JFileChooser fileChooser = new JFileChooser();
		    	JFrame frame = new JFrame();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				frame.dispose();
				if (result == JFileChooser.APPROVE_OPTION) {
					main.setGraphe(exporter.lireGraphe(fileChooser.getSelectedFile()));
				}
			}
		}
    }
}
