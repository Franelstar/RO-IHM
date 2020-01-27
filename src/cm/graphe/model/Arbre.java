package cm.graphe.model;
import java.util.ArrayList;

public class Arbre implements Comparable<Arbre> {
	protected int id;
	protected Arbre parent;
	protected ArrayList<Arbre> enfants;
	
	/** Constructeurs */
	public Arbre(int i) {
          // a completer
	}
	
	public Arbre(int i, ArrayList<Arbre> a) {
          // a completer
	}
	
	public Arbre(int i, Arbre[] a) {
          // a completer
	}
	/** Fin constructeurs */
	
	// Une feuille n'a pas d'enfants
	public boolean estFeuille() {
          // a completer
		
		return true;
	}
	
	// modifier pere
	public boolean ajouteParent(Arbre p) {
          // a completer
		
		return true;
	}
	
	// Ne rien faire si a est deja un enfant
	public boolean ajouteEnfant(Arbre a) {
          // a completer
		
		return true;
	}

	public int nbEnfants() {
          // a completer
		
		return 1;
	}
	
	// Le nombre total de noeuds
	public int size() {
          // a completer
		return 1;
	}
	
	@Override
	public int compareTo(Arbre a) {
		// TODO Auto-generated method stub
		if ( id > a.id ) {
			return 1;
		}
		else if ( id < a.id ) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
