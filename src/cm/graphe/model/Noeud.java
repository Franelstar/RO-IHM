package cm.graphe.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <b>Class Noeud</b><br><br>
 * 
 * Cette classe permet de créer une instance de <b>noeud</b> pour un graphe.
 * Ce noeud peut être utilisé pour tous les types de graphe.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Graphe
 * 
 * @version 1.0
 */
public class Noeud {
	protected String id = new Timestamp(System.currentTimeMillis()).toString(); //Identifiant du noeud
	protected int nbVoisins;
	protected StringProperty label = new SimpleStringProperty(); //Nom du noeud (est unique pour un noeud)
	protected ArrayList<Noeud>  successeurs = new ArrayList<Noeud>(); //Liste des noeuds successeur
	protected ArrayList<Integer> arcs = new ArrayList<>(); //Poids des noeuds successeurs 
	
	/**
	 * Création d'un noeud à partir de son nom.<br>
	 * Le nombre de voisin dès la création sera 0.<br>
	 * 
	 * @param l Nom du noeud à créer
	 */
	public Noeud(String l) {
		label.set(l);
		nbVoisins = 0;
	}
	
	/**
	 * Création d'un noeud à partir de son nom et de ses noeuds voisins.<br>
	 * Le nombre de voisin dès la création sera donné dans la variable i.<br>
	 * La valeur de i doit correspondre avec le nombre de voisin dans la variable noeuds. Dans le cas contraire, le noeud ne sera pas créé.<br>
	 * 
	 * @param l Nom du noeud à créer
	 * @param i Nombre de voisin
	 * @param noeuds Liste des noeuds voisins
	 */
	public Noeud(String l, int i, Noeud[] noeuds) {
		if(i != noeuds.length) {
			label.set(l);
			nbVoisins = i;
			for(Noeud nd : noeuds) {
				successeurs.add(nd);
				arcs.add(1);
			}
		}
		else {
			System.out.println("Vérifiez le nombre de voisin !");
		}
	}
	
	/**
	 * <b>getId</b><br><br>
	 * 
	 * Retourne l'identifiant du noeud.<br>
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
	 * Retourne le nom du noeud.<br>
	 * Le nom est une chaine de caractère.<br>
	 * 
	 * @return String
	 */
	public StringProperty getLabel() {
		return label;
	}
	
	/**
	 * <b>setLabel</b><br><br>
	 * 
	 * Modifie le nom du noeud.<br>
	 * Le nom doit être une chaine de caractère.<br>
	 * 
	 * @param label Nouveau nom du noeud
	 */
	public void setLabel(StringProperty label) {
		this.label = label;
	}
	
	/**
	 * <b>getNbVoisins</b><br><br>
	 * 
	 * Retourne le nombre de voisin du noeud.<br>
	 * Pour un graphe orienté, retourne le nombre de voisin sortant.<br>
	 * 
	 * @return Integer
	 */
	public int getNbVoisins() {
		return this.nbVoisins;
	}
	
	/**
	 * <b>estVoisin</b><br><br>
	 * 
	 * Retourne le nom de tous les voisins du noeud.<br>
	 * Si ce noeud ne contient aucun voisin, on retourne null.<br>
	 * 
	 * @return String ou null
	 */
	public String estVoisin() {
		if(this.successeurs.size() > 0) {
			String listeVoisins = new String("Les voisins sont :\n");
			for(Noeud noe : successeurs) {
				listeVoisins += noe.getLabel();
				listeVoisins += "\n";
			}
			return listeVoisins;
		}
		return null;
	}
	
	/**
	 * <b>estVoisin</b><br><br>
	 * 
	 * Vérifie si un noeud est voisin du noeud.<br>
	 * Si le noeud donné en paramètre est voisin de ce noeud, on retourne son index dans la liste des voisins. Dans le cas contraire on retourne -1.<br>
	 * 
	 * @param v Noeud à vérifier comme voisin
	 * 
	 * @return Integer
	 */
	public int estVoisin(Noeud v) {
		//a completer
		if(this.successeurs.size() > 0) {
			if(this.successeurs.contains(v)) {
				return this.successeurs.indexOf(v);
			}
		}
		return -1;
	}
	
	/**
	 * <b>ajouteVoisin</b><br><br>
	 * 
	 * Ajouter un voisin du noeud.<br>
	 * si v est deja voisin, on modifie la poids correspondant.<br>
	 * revoie le nouveau nombre de voisins<br>
	 * 
	 * @param v Noeud à ajouter comme voisin
	 * @param d Poids
	 * 
	 * @return Integer (Nombre de voisin)
	 */
	public int ajouteVoisin(Noeud v, int d) {
		if(this.successeurs.contains(v)) { //Si v est deja voisin
			int i = this.successeurs.indexOf(v); //On recupère son indice
			this.arcs.set(i, d); //On modifie son poids
		}
		else {
			this.successeurs.add(v); //On ajoute le voisin
			this.arcs.add(d); //On modifie le poids
			this.nbVoisins++; //On met à jour le nombre de voisin
		}
		return this.successeurs.size();
	}
	
	/**
	 * <b>enleveVoisin</b><br><br>
	 * 
	 * Supprimer un voisin du noeud.<br>
	 * si v n'est pas voisin, on ne fait rien.<br>
	 * revoie le nouveau nombre de voisins<br>
	 * 
	 * @param v Noeud à enlever
	 * 
	 * @return Integer (Nombre de voisin)
	 */
	public int enleveVoisin(Noeud v) {
		//a completer
		if(this.successeurs.contains(v)) {
			int i = this.successeurs.indexOf(v);
			this.successeurs.remove(i);
			this.arcs.remove(i);
			this.nbVoisins--;
		}
		return this.successeurs.size();
	}
	
	/**
	 * <b>compareTo</b><br><br>
	 * 
	 * Comparer deux noeuds.<br>
	 * si les deux noeuds son les même, on retourne true, sinon on retourne false.<br>
	 * 
	 * @param o Noeud à comparer
	 * 
	 * @return Boolean
	 */
	public Boolean compareTo(Noeud o) {
		return o.getId().equals(getId());
	}
	
	/**
	 * <b>getSuccesseurs</b><br><br>
	 * 
	 * Retourne la liste des successeurs du noeud.<br>
	 * 
	 * @return ArrayList de Noeuds
	 */
	public ArrayList<Noeud> getSuccesseurs(){
		return successeurs;
	}
	
	/**
	 * <b>getPoidsUnSucesseur</b><br><br>
	 * 
	 * Retourne le poids d'un voisin dont on connait l'identifiant.<br>
	 * Retourne 0 si le noeud n'est pas voisin.<br>
	 * 
	 * @param id Identifiant du voisin dont on veut le poids
	 * 
	 * @return Integer
	 */
	public int getPoidsUnSucesseur(String id) {
		for(Noeud suc : successeurs) {
			if(suc.getId().equals(id)) {
				return arcs.get(successeurs.indexOf(suc));
			}
		}
		return 0;
	}
	
	/**
	 * <b>getSuccesseursToString</b><br><br>
	 * 
	 * Retourne la liste des successeurs formatée.<br>
	 * 
	 * @return SimpleStringProperty
	 */
	public StringProperty getSuccesseursToString() {
		String retour = "[ ";
		for(int i = 0; i < successeurs.size(); i++) {
			retour += successeurs.get(i).getLabel().get() + "(" + arcs.get(i) + ")";
			if(i+1 < successeurs.size())
				retour += " | ";
		}
		retour += " ]";
		return new SimpleStringProperty(retour);
	}

}
