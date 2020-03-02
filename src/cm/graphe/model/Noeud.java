package cm.graphe.model;

import java.sql.Timestamp;

/**
 * @author franel
 *
 */

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Noeud implements Comparable<Noeud> {
	protected String id = new Timestamp(System.currentTimeMillis()).toString();
	protected int nbVoisins;
	protected StringProperty label = new SimpleStringProperty();
	// c'est plus simple d'ajouter et d'enlever des elements avec une liste qu'avec un array
	protected ArrayList<Noeud>  successeurs = new ArrayList<Noeud>();
	protected ArrayList<Integer> arcs = new ArrayList<>(); //arcs sortant 
	
	// cree un noeud isole
	public Noeud(String l) {
		//a completer
		label.set(l);
		nbVoisins = 0;
	}
	
	// cree un noeud a partir d'une liste de voisins
	// les arcs sont de poids 1
	public Noeud(String l, int i, Noeud[] noeuds) {
		//a completer
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
	
	public String getId() {
		return id;
	}
	
	public void setId() {
		
	}
	
	public StringProperty getLabel() {
		return label;
	}
	
	public void setLabel(StringProperty label) {
		this.label = label;
	}
	
	public int getNbVoisins() {
		return this.nbVoisins;
	}
	
	public void setNbVoisins(int NbreVoisin) {
		this.nbVoisins = NbreVoisin;
	}
	
	// renvoie le degre sortant, qui vaut la somme des elements de arcs
	// @param:
	public int degreSortant() {
		return this.arcs.size();
	}
	
	// renvoie la liste des voisins
	// @param:
	public String estVoisin() {
		if(this.successeurs.size() > 0) {
			String listeVoisins = new String("Les voisins sont :\n");
			for(Noeud noe : successeurs) {
				listeVoisins += noe.getLabel();
				listeVoisins += "\n";
			}
			return listeVoisins;
		}
		return "Aucun voisin";
	}
	
	// renvoie l'indice de v dans voisins si v est voisin
	// renvoie -1 si v n'est pas voisin
	// @param: v = noeud à vérifier
	public int estVoisin(Noeud v) {
		//a completer
		if(this.successeurs.size() > 0) {
			if(this.successeurs.contains(v)) {
				return this.successeurs.indexOf(v);
			}
		}
		return -1;
	}
	
	// renvoie le poids de l'arc correspondant si v est voisin, 0 sinon
	// @param: v
	public int nbArcs(Noeud v) {
		//a completer
		if(this.successeurs.size() > 0) {
			if(this.successeurs.contains(v)) {
				int a = this.successeurs.indexOf(v);
				return this.arcs.get(a);
			}
		}
		return 0;
	}
	
	// si v est deja voisin, modifie la poids de l'arc correspondant
	// sinon, ajoute v comme un nouveau voisin avec un arc de poids d
	// (il faut incrementer nbArcs si c'est un nouveau voisin)
	// revoie le nouveau nombre de voisins
	// @param: v = noeud à ajouter
	//         d = le poids
	public int ajouteVoisin(Noeud v, int d) {
		//a completer
		if(this.successeurs.contains(v)) {
			int i = this.successeurs.indexOf(v);
			this.arcs.set(i, d);
		}
		else {
			this.successeurs.add(v);
			this.arcs.add(d);
			this.nbVoisins++;
		}
		return this.successeurs.size();
	}
	
	// si v n'est pas voisin, ne fait rien
	// sinon enleve v de la liste de voisins
	// renvoie le nouveau nombre de voisins
	// @param: v = noeud à enlever
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
	
	@Override
	public int compareTo(Noeud o) {
		if ( label == o.getLabel()) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public ArrayList<Noeud> getSuccesseurs(){
		return successeurs;
	}
	
	public boolean estSuccesseur(String v) {
		if(successeurs.size() > 0) {
			for(Noeud n : successeurs) {
				if(n.label.get().equals(v))
					return true;
			}
		}
		
		return false;
	}
	
	public String getSuccesseursToString() {
		String retour = "[ ";
		for(int i = 0; i < successeurs.size(); i++) {
			retour += successeurs.get(i).getLabel().get();
			if(i+1 < successeurs.size())
				retour += " | ";
		}
		retour += " ]";
		return retour;
	}

}
