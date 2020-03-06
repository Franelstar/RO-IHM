package cm.graphe.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * <b>Class Graphe</b><br><br>
 * 
 * Cette classe permet de créer une instance de <b>Graphe</b>.
 * Un graphe est un ensemble de noeuds structurés d'une certaine façon.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Noeud
 * 
 * @version 1.0
 */
public class Graphe{
	protected StringProperty nom = new SimpleStringProperty(); //Identifiant du graphe
	protected int nbNoeuds;
	protected ObservableList<Noeud> noeuds = FXCollections.observableArrayList(); //Liste des noeuds
	protected TypeGraphe typeGraphe; //Type de graphe
	
	/**
	 * Création d'un graphe à partir de son nom et de son type.<br>
	 * Le nombre de noeud dès la création sera 0.<br>
	 * 
	 * @param n Nom du graphe à créer
	 * @param t Type du graphe à créer
	 * 
	 * @see TypeGraphe
	 */
	public Graphe(String n, TypeGraphe t) {
		nom.setValue(n);
		nbNoeuds = 0;
		typeGraphe = t;
	}
	
	/**
	 * Création d'un graphe à partir de son nom, de son type et de la liste de ses noeuds.<br>
	 * 
	 * @param n Nom du graphe à créer
	 * @param t Type du graphe à créer
	 * @param nds Liste des noeuds
	 * 
	 * @see TypeGraphe
	 */
	public Graphe(Noeud[] nds, String n, TypeGraphe t) {
		nom.set(n);
		nbNoeuds = nds.length;
		for(Noeud nd : nds) {
			noeuds.add(nd);
		}
		
		typeGraphe = t;
	}
	
	/**
	 * <b>getTypeGraphe</b><br><br>
	 * 
	 * Retourne le type du graphe.<br>
	 * 
	 * @return TypeGraphe
	 * 
	 * @see TypeGraphe
	 */
	public TypeGraphe getTypeGraphe() {
		return typeGraphe;
	}
	
	/**
	 * <b>setTypeGraphe</b><br><br>
	 * 
	 * Changer le type du graphe.<br>
	 * 
	 * @see TypeGraphe
	 */
	public void setTypeGraphe(TypeGraphe t) {
		typeGraphe = t;
	}
	
	/**
	 * <b>getNom</b><br><br>
	 * 
	 * Retourne le nom du graphe.<br>
	 * 
	 * @return String
	 */
	public StringProperty getNom() {
		return nom;
	}
	
	/**
	 * <b>setNom</b><br><br>
	 * 
	 * Change le nom du graphe.<br>
	 * 
	 * @param nom Nouveau nom
	 */
	public void setNom(StringProperty nom) {
		this.nom = nom;
	}
	
	/**
	 * <b>getNbNoeuds</b><br><br>
	 * 
	 * Retourne le nombre de noeuds du graphe.<br>
	 * 
	 * @return Int
	 */
	public int getNbNoeuds() {
		return nbNoeuds;
	}
	
	/**
	 * <b>creerNoeud</b><br><br>
	 * 
	 * Créer un nouveau noeud.<br>
	 * Ce nouveau noeud ne doit pas avoir un nom identique à un noeud déjà existant.<br>
	 * 
	 * @param neu Noeud à créer
	 */
	public void creerNoeud(Noeud neu) {
		if(!contentNoeud(neu.getLabel().get())) {
			noeuds.add(neu);
			nbNoeuds = noeuds.size();
		}
	}
	
	/**
	 * <b>getNoeuds</b><br><br>
	 * 
	 * Retourne la liste de noeuds formatée.<br>
	 * 
	 * @return String
	 */
	public String getNoeuds() {
		String r = "";
		for(Noeud nd : noeuds)
			r += nd.getLabel()+" | ";
		return r;
	}
	
	/**
	 * <b>getNoeud</b><br><br>
	 * 
	 * Retourne un noeud en fontion de son nom.<br>
	 * Si ce noeud n'existe pas, on retourne null.<br>
	 * 
	 * @param nom Nom du noeud à retourner
	 * 
	 * @return Noeud ou null
	 */
	public Noeud getNoeud(String nom) {
		for(Noeud nd : noeuds) {
			if(nd.getLabel().get().equals(nom)) {
				return nd;
			}
		}
		return null;
	}
	
	/**
	 * <b>getNoeudIndex</b><br><br>
	 * 
	 * Retourne un noeud en fontion de son index dans la liste des noeuds.<br>
	 * Si cet index n'existe pas, on retourne null.<br>
	 * 
	 * @param index Index du noeud à retourner
	 * 
	 * @return Noeud ou null
	 */
	public Noeud getNoeudIndex(int index) {
		if(contentNoeudIndex(index)) {
			return noeuds.get(index);
		}
		return null;
	}
		
	/**
	 * <b>getNoeudId</b><br><br>
	 * 
	 * Retourne l'index d'un noeud dans la liste des noeuds en fontion de son identifiant.<br>
	 * Si cet identifiant n'existe pas, on retourne -1.<br>
	 * 
	 * @param id Identifiant du noeud dont on veut l'index
	 * 
	 * @return Integer
	 */
	public int getNoeudId(String id) {
		for(Noeud nd : noeuds) {
			if(nd.getId().equals(id)) {
				return noeuds.indexOf(nd);
			}
		}
		return -1;
	}
	
	/**
	 * <b>getNoeudLabelToIndex</b><br><br>
	 * 
	 * Retourne l'index d'un noeud dans la liste des noeuds en fontion de son nom.<br>
	 * Si ce nom n'existe pas, on retourne -1.<br>
	 * 
	 * @param n nom du noeud dont on veut l'index
	 * 
	 * @return Integer
	 */
	public int getNoeudLabelToIndex(String n) {
		for(Noeud nd : noeuds) {
			if(nd.getLabel().get().equals(n)) {
				return noeuds.indexOf(nd);
			}
		}
		return -1;
	}
	
	/**
	 * <b>contentNoeud</b><br><br>
	 * 
	 * Vérifie si un noeud appatient au graphe.<br>
	 * En entrée on utilise le nom du noeud.<br>
	 * Si ce noeud existe, on retourne true. Dans le cas contraire on retourne false.<br>
	 * 
	 * @param c Nom du noeud
	 * 
	 * @return Boolean
	 */
	public boolean contentNoeud(String c) {
		for(Noeud nd : noeuds) {
			if(nd.getLabel().get().equals(c))
				return true;
		}
		return false;
	}
	
	/**
	 * <b>contentNoeudIndex</b><br><br>
	 * 
	 * Vérifie si un noeud appatient au graphe.<br>
	 * En entrée on utilise l'index du noeud.<br>
	 * Si ce noeud existe, on retourne true. Dans le cas contraire on retourne false.<br>
	 * 
	 * @param c Index du noeud
	 * 
	 * @return Boolean
	 */
	public boolean contentNoeudIndex(int c) {
		for(int i = 0; i < noeuds.size(); i++) {
			if(i == c)
				return true;
		}
		return false;
	}
	
	/**
	 * <b>deleteNoeud</b><br><br>
	 * 
	 * Supprimer un noeud du graphe à partir de l'index du noeud dans la liste de noeuds.<br>
	 * Si le noeud n'existe pas, on ne fait rien.<br>
	 * 
	 * @param index Index du noeud à supprimer
	 */
	public void deleteNoeud(int index) {
		if(contentNoeudIndex(index)) {
			for(Noeud n : noeuds) {
				n.enleveVoisin(getNoeudIndex(index));
			}
			noeuds.remove(index);
			this.nbNoeuds--;
		}
	}
	
	/**
	 * <b>getListeNoeud</b><br><br>
	 * 
	 * Retourne la liste des noeuds.<br>
	 * 
	 * @return ObservableList de noeuds
	 */
	public ObservableList<Noeud> getListeNoeud(){
		return this.noeuds;
	}
}
