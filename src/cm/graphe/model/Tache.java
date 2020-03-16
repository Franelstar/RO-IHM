package cm.graphe.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <b>Class Tache</b><br><br>
 * 
 * Cette classe permet de créer une instance de <b>tache</b>.
 * 
 * @author Franck Anael MBIAYA
 * 
 * @version 1.0
 */
public class Tache {
	protected String id = new Timestamp(System.currentTimeMillis()).toString(); //Identifiant de la tache
	protected int duree;
	protected StringProperty label = new SimpleStringProperty(); //Nom de la tache (est unique pour une tache)
	protected StringProperty libelle = new SimpleStringProperty(); //Libelle de la tache
	protected ArrayList<Tache>  predecesseurs = new ArrayList<Tache>(); //Liste des taches prédécesseurs
	
	/**
	 * Création d'une tache.<br>
	 * 
	 * @param l Nom du noeud à créer
	 */
	public Tache(String label, String libelle, int duree) {
		this.label.set(label);
		this.libelle.set(libelle);
		this.duree = duree;
	}
	
	/**
	 * <b>getId</b><br><br>
	 * 
	 * Retourne l'identifiant de la tache.<br>
	 * L'identifiant est une chaine de caractère.<br>
	 * 
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * <b>getLabel</b><br><br>
	 * 
	 * Retourne le label de la tache.<br>
	 * 
	 * @return String
	 */
	public String getLabel() {
		return label.get();
	}
	
	/**
	 * <b>setLabel</b><br><br>
	 * 
	 * Modifie le label de la tache.<br>
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label.set(label);
	}
	
	/**
	 * <b>getLibelle</b><br><br>
	 * 
	 * Retourne le libelle de la tache.<br>
	 * 
	 * @return String
	 */
	public String getLibelle() {
		return libelle.get();
	}
	
	/**
	 * <b>setLibelle</b><br><br>
	 * 
	 * Modifie le libelle de la tache.<br>
	 * 
	 * @param libelle
	 */
	public void setLibelle(String libelle) {
		this.libelle.set(libelle);
	}
	
	/**
	 * <b>getDuree</b><br><br>
	 * 
	 * Retourne la durée de la tache.<br>
	 * 
	 * @return Integer
	 */
	public Integer getDuree() {
		return duree;
	}
	
	/**
	 * <b>setDuree</b><br><br>
	 * 
	 * Modifie la durée de la tache.<br>
	 * 
	 * @param duree
	 */
	public void setDuree(int duree) {
		this.duree = duree;
	}
	
	/**
	 * <b>getPredecesseurs</b><br><br>
	 * 
	 * Retourne la liste de predeceddeurs.<br>
	 * 
	 * @return ArrayList<Tache>
	 */
	public ArrayList<Tache> getPredecesseurs() {
		return this.predecesseurs;
	}
	
	/**
	 * <b>ajouterPredecesseur</b><br><br>
	 * 
	 * Ajoute un predecesseur.<br>
	 * 
	 * @param tache Tache utilisé comme prédécesseur
	 */
	public void ajouterPredecesseur(Tache tache) {
		if(!this.predecesseurs.contains(tache))
			this.predecesseurs.add(tache);
	}
	
	/**
	 * <b>enleverPredecesseur</b><br><br>
	 * 
	 * Ajoute un predecesseur.<br>
	 * 
	 * @param tache Tache utilisé comme prédécesseur
	 */
	public void enleverPredecesseur(Tache tache) {
		if(!this.predecesseurs.contains(tache))
			this.predecesseurs.remove(tache);
	}
}
