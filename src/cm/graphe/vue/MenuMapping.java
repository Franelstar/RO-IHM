/**
 * 
 */
package cm.graphe.vue;

import cm.graphe.MainClass;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    
    //Méthode qui sera utilisée dans l'initialisation de l'IHM
    //dans notre classe principale
    public void setMainApp(MainClass mainApp) {
        this.main = mainApp;
    }
    
	//Fermer l'application
	@FXML
	public void fermer() {
		//On affiche un message car on est poli.
		Alert bye = new Alert(AlertType.INFORMATION);
		bye.setTitle("Au revoir !");
		bye.setHeaderText("See you soon...");
		bye.setContentText("A bientõt");
		bye.showAndWait();
		
		//Et on clos le stage principal, donc l'application
		this.main.getStage().close();
	}
	
	@FXML
	public void nouveau() {
		
	}
}
