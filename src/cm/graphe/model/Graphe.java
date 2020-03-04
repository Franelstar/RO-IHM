package cm.graphe.model;

/**
 * @author Franck Anael MBIAYA
 *
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Graphe {
	protected StringProperty nom = new SimpleStringProperty();
	protected int nbNoeuds;
	protected ObservableList<Noeud> noeuds = FXCollections.observableArrayList();
	protected TypeGraphe typeGraphe;
	
	public Graphe(String n, TypeGraphe t) {
		//a completer
		nom.setValue(n);
		nbNoeuds = 0;
		typeGraphe = t;
	}
	
	public Graphe(Noeud[] nds, String n, TypeGraphe t) {
		nom.set(n);
		nbNoeuds = nds.length;
		for(Noeud nd : nds) {
			noeuds.add(nd);
		}
		
		typeGraphe = t;
	}
	
	public TypeGraphe getTypeGraphe() {
		return typeGraphe;
	}
	
	public void setTypeGraphe(TypeGraphe t) {
		typeGraphe = t;
	}
	
	public StringProperty getNom() {
		return nom;
	}
	
	public void setNom(StringProperty nom) {
		this.nom = nom;
	}
	
	public int getNbNoeuds() {
		return nbNoeuds;
	}
	
	public void setNbNoeuds(int nbNoeuds) {
		this.nbNoeuds = nbNoeuds;
	}
	
	public void creerNoeud(Noeud neu) {
		if(!contentNoeud(neu.getLabel().get())) {
			noeuds.add(neu);
			nbNoeuds = noeuds.size();
		}
	}
	
	// Retourne la liste des noeuds préformaté
	// @ param
	public String getNoeuds() {
		String r = "";
		for(Noeud nd : noeuds)
			r += nd.getLabel()+" | ";
		return r;
	}
	
	// Retourne un noeud en fonction du nom du noeud
	// @param : nom = label du noeud
	public Noeud getNoeud(String nom) {
		for(Noeud nd : noeuds) {
			if(nd.getLabel().get().equals(nom)) {
				return nd;
			}
		}
		return null;
	}
	
	// Retourne un noeud en fonction de l'index du noeud
	// @param : nom = label du noeud
	public Noeud getNoeudIndex(int index) {
		if(contentNoeudIndex(index)) {
			return noeuds.get(index);
		}
		return null;
	}
		
	// Retourne un index en fonction de l'ID du noeud
	// @param : nom = label du noeud
	public int getNoeudId(String id) {
		for(Noeud nd : noeuds) {
			if(nd.getId().equals(id)) {
				return noeuds.indexOf(nd);
			}
		}
		return -1;
	}
	
	// Retourne un index en fonction du label du noeud
	// @param : nom = label du noeud
	public int getNoeudLabelToIndex(String n) {
		for(Noeud nd : noeuds) {
			if(nd.getLabel().get().equals(n)) {
				return noeuds.indexOf(nd);
			}
		}
		return -1;
	}
	
	// Vérifie si un noeud fait partir de la liste des noeuds
	// @param : c = label du noeud
	public boolean contentNoeud(String c) {
		for(Noeud nd : noeuds) {
			if(nd.getLabel().get().equals(c))
				return true;
		}
		return false;
	}
	
	// Vérifie si un index identifie un noeud
	// @param : c = index du noeud
	public boolean contentNoeudIndex(int c) {
		for(int i = 0; i < noeuds.size(); i++) {
			if(i == c)
				return true;
		}
		return false;
	}
	
	//Supprimer un noeud
	//@param : neu = noeud à supprimer
	public void deleteNoeud(int index) {
		if(contentNoeudIndex(index)) {
			for(Noeud n : noeuds) {
				n.enleveVoisin(getNoeudIndex(index));
			}
			noeuds.remove(index);
			this.nbNoeuds--;
		}
	}
	
	// Retourne la liste des noeuds
	public ObservableList<Noeud> getListeNoeud(){
		return this.noeuds;
	}
	
	public void affiche() {
    //a completer
	}	
}
