package cm.graphe.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
	protected IntegerProperty duree = new SimpleIntegerProperty();
	protected StringProperty label = new SimpleStringProperty(); //Nom de la tache (est unique pour une tache)
	protected StringProperty libelle = new SimpleStringProperty(); //Libelle de la tache
	protected ArrayList<Tache>  predecesseurs = new ArrayList<Tache>(); //Liste des taches prédécesseurs
	
	public static int instanceCount = 0;
	
	{ // Constructeur implicite
		  instanceCount++;
	}
	
	/**
	 * Création d'une tache.<br>
	 * 
	 * @param l Nom du noeud à créer
	 */
	public Tache(String label, String libelle, int duree) {
		this.label.set(label);
		this.libelle.set(libelle);
		this.duree.setValue(duree);
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
	public StringProperty getLabel() {
		return label;
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
	public StringProperty getLibelle() {
		return libelle;
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
	public IntegerProperty getDuree() {
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
		this.duree.setValue(duree);
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
	 * <b>estPredecesseur</b><br><br>
	 * 
	 * Retourne la liste de predeceddeurs.<br>
	 * 
	 * @param t tache à vérifier
	 * 
	 * @return Bolean
	 */
	public Boolean estPredecesseur(Tache t) {
		if(this.predecesseurs.contains(t))
			return true;
		
		return false;
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
		if(this.predecesseurs.contains(tache))
			this.predecesseurs.remove(tache);
	}
	
	/**
	 * <b>getPredecesseursToString</b><br><br>
	 * 
	 * Retourne la liste des prédécesseurs formatée.<br>
	 * 
	 * @return SimpleStringProperty
	 */
	public StringProperty getPredecesseursToString() {
		String retour = "[ ";
		for(int i = 0; i < predecesseurs.size(); i++) {
			retour += predecesseurs.get(i).getLabel().get();
			if(i+1 < predecesseurs.size())
				retour += " | ";
		}
		retour += " ]";
		return new SimpleStringProperty(retour);
	}
}
