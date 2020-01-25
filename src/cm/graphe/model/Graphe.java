package cm.graphe.model;

/**
 * @author Franck Anael MBIAYA
 *
 */

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Graphe {
	protected StringProperty nom = new SimpleStringProperty();;
	protected int nbNoeuds;
	protected ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
	
	public Graphe(String n) {
		//a completer
		nom.setValue(n);
		nbNoeuds = 0;
	}
	
	public Graphe(Noeud[] nds, String n) {
		nom.set(n);
		nbNoeuds = nds.length;
		for(Noeud nd : nds) {
			noeuds.add(nd);
		}
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
	@SuppressWarnings("unlikely-arg-type")
	public Noeud getNoeud(String nom) {
		for(Noeud nd : noeuds) {
			if(nd.label.equals(nom)) {
				return nd;
			}
		}
		return null;
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
	public void deleteNoeud(Noeud neu) {
		for(Noeud n : noeuds) {
			n.enleveVoisin(neu);
		}
		noeuds.remove(noeuds.indexOf(neu));
		this.nbNoeuds--;
	}
	
	// Retourne la liste des noeuds
	public ArrayList<Noeud> getListeNoeud(){
		return this.noeuds;
	}
	
	public void affiche() {
    //a completer
	}	
}
