package cm.graphe.model;
import java.util.ArrayList;

/**
 * <b>Class Arbre</b><br><br>
 * 
 * Cette classe permet de créer une instance de <b>Arbre</b>.
 * Un arbre est généré à partir d'un graphe.<br>
 * 
 * @author Franck Anael MBIAYA
 * 
 * @see Graphe
 * 
 * @version 1.0
 */
public class Arbre implements Comparable<Arbre> {
	protected String id; //Identifiant de l'arbre
	protected Arbre parent; //arbre parent
	protected String label; //Nom de l'arbre
	protected ArrayList<Arbre> enfants = new ArrayList<Arbre>(); //Liste des enfants
	
	/**
	 * Création d'un arbre à partir de son nom.<br>
	 * Par défaut, il a aucin enfant et pas de parent<br>
	 * 
	 * @param i Identifiant de l'arbre.
	 */
	public Arbre(String i) {
          id = i;
	}
	
	/**
	 * <b>getId</b><br><br>
	 * 
	 * Retourne l'identifiant de l'arbre.<br>
	 * 
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * <b>setId</b><br><br>
	 * 
	 * Modifie l'identifiant de l'arbre.<br>
	 * 
	 * @param newId Nouvel identifiant de l'arbre
	 */
	public void setId(String newId) {
		id = newId;
	}
	
	/**
	 * <b>getLabel</b><br><br>
	 * 
	 * Retourne le nom de l'arbre.<br>
	 * 
	 * @return String
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * <b>setLabel</b><br><br>
	 * 
	 * Modifie le nom de l'arbre.<br>
	 * 
	 * @param newLabel Nouveau nom de l'arbre
	 */
	public void setLabel(String newLabel) {
		label = newLabel;
	}
	
	/**
	 * <b>getEnfants</b><br><br>
	 * 
	 * Retourne la liste des enfants.<br>
	 * 
	 * @return ArrayList de arbre
	 */
	public ArrayList<Arbre> getEnfants() {
		return enfants;
	}
	
	/**
	 * <b>getParent</b><br><br>
	 * 
	 * Retourne le parent.<br>
	 * 
	 * @return Arbre
	 */
	public Arbre getParent() {
		return parent;
	}
	
	/**
	 * <b>estFeuille</b><br><br>
	 * 
	 * Verifie si un arbre est enfant direct.<br>
	 * Retourne True s'il est enfant et false sinon.<br>
	 * 
	 * @param enfant Arbre à vérifier s'il est enfant
	 * 
	 * @return Bollean
	 */
	public boolean estFeuille(Arbre enfant) {
          return enfants.contains(enfant);
	}
	
	/**
	 * <b>ajouteParent</b><br><br>
	 * 
	 * Modifier le parent.<br>
	 * 
	 * @param p Arbre parent
	 */
	public void ajouteParent(Arbre p) {
		parent = p;
	}
	
	/**
	 * <b>ajouteEnfant</b><br><br>
	 * 
	 * Ajouter un arbre enfant.<br>
	 * Si l'arbre est déjà enfant, on ne fait rien.<br>
	 * 
	 * @param a Arbre parent
	 */
	public void ajouteEnfant(Arbre a) {
		if(!enfants.contains(a))
          enfants.add(a);
	}

	/**
	 * <b>nbEnfants</b><br><br>
	 * 
	 * Retourne le nombre d'enfant.<br>
	 * 
	 * @return Integer
	 */
	public int nbEnfants() {
		return enfants.size();
	}
	
	/**
	 * <b>compareTo</b><br><br>
	 * 
	 * Compare deux arbres.<br>
	 * 
	 * @param a Arbre à comparer
	 * 
	 * @return Integer
	 */
	public int compareTo(Arbre a) {
		if ( id.equals(a.getId()) ) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
