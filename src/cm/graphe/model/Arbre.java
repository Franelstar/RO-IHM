package cm.graphe.model;
import java.util.ArrayList;

public class Arbre implements Comparable<Arbre> {
	protected String id;
	protected Arbre parent;
	protected String label;
	protected ArrayList<Arbre> enfants = new ArrayList<Arbre>();
	
	/** Constructeurs */
	public Arbre(String i) {
          id = i;
	}
	
	public Arbre(int i, ArrayList<Arbre> a) {
          // a completer
	}
	
	public Arbre(int i, Arbre[] a) {
          // a completer
	}
	/** Fin constructeurs */
	
	public String getId() {
		return id;
	}
	
	public void setId(String newId) {
		id = newId;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String newLabel) {
		label = newLabel;
	}
	
	public ArrayList<Arbre> getEnfants() {
		return enfants;
	}
	
	public Arbre getParent() {
		return parent;
	}
	
	public ArrayList<Arbre> getEnfant() {
		return enfants;
	}
	
	// Une feuille n'a pas d'enfants
	public boolean estFeuille() {
          // a completer
		
		return true;
	}
	
	// modifier pere
	public boolean ajouteParent(Arbre p) {
		parent = p;
		
		return true;
	}
	
	// Ne rien faire si a est deja un enfant
	public boolean ajouteEnfant(Arbre a) {
          enfants.add(a);
		
		return true;
	}

	public int nbEnfants() {
		return enfants.size();
	}
	
	// Le nombre total de noeuds
	public int size() {
          // a completer
		return 1;
	}
	
	@Override
	public int compareTo(Arbre a) {
		// TODO Auto-generated method stub
		if ( id.equals(a.getId()) ) {
			return 1;
		}
		else if ( id.equals(a.getId()) ) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
