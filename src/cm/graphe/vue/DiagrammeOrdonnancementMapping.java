package cm.graphe.vue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cm.graphe.model.Noeud;
import cm.graphe.model.Tache;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class DiagrammeOrdonnancementMapping extends Application {
	final NumberAxis xAxis = new NumberAxis();
    final CategoryAxis yAxis = new CategoryAxis();
	@FXML
    final LineChart<Number,String> diagramme = 
            new LineChart<Number,String>(xAxis,yAxis);
	
	List<HashMap<Noeud, Double>> liste;
	ObservableList<Tache> taches;
	
	public void setListe(List<HashMap<Noeud, Double>> l) {
		liste = l;
	}
	
	public void setTaches(ObservableList<Tache> t) {
		taches = t;
	}
	
	@Override public void start(Stage stage) {
		
		HashMap<Noeud, Double> maxDebut = liste.get(0);
		HashMap<Noeud, Double> MaxTot = liste.get(1);
		
		//ensuite on classe les noeuds dans liste par ordre croissant
		Comparator<Entry<Noeud, Double>> valueComparator = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
		Map<Noeud, Double> minDebut = 
				MaxTot.entrySet().stream().
			    sorted(valueComparator).
			    collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                             (e1, e2) -> e1, LinkedHashMap::new));
		
		
        stage.setTitle("Ordonnancement");
        //defining the axes
        
        xAxis.setLabel("Durée");
        xAxis.setSide(Side.TOP);xAxis.setTickLabelRotation(30);
        yAxis.setLabel("Liste des tâches");
        //creating the chart
        
                
        //defining a series
        ArrayList<XYChart.Series> series = new ArrayList<XYChart.Series>();
        for (Noeud key : minDebut.keySet()) {
        	if(!key.getLabel().get().equals("S") && !key.getLabel().get().equals("P")) {
	        	 XYChart.Series s = new XYChart.Series();
	        	 Tache tache = null;
	        	 //On cherche la tâche
	        	 for(Tache t : taches) {
	        		 if(t.getLabel().get().equals(key.getLabel().get())) {
	        			 tache = t;
	        		 }
	        	 }
	        	 
	        	 s.setName(key.getLabel().get());
	        	 s.getData().add(new XYChart.Data(minDebut.get(key), key.getLabel().get()));
	        	 s.getData().add(new XYChart.Data(minDebut.get(key) + tache.getDuree().get(), key.getLabel().get()));
	        	 diagramme.getData().add(s);
        	}
        }
        
        
        Scene scene  = new Scene(diagramme,800,600);
       
        stage.setScene(scene);
        stage.show();
    }
}
