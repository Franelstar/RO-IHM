/**
 * @author Franck Anael MBIAYA
 *
 */

package cm.graphe.controler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;

import cm.graphe.model.Arbre;
import cm.graphe.model.Graphe;
import cm.graphe.model.Noeud;
import cm.graphe.model.TypeGraphe;
import javafx.beans.property.SimpleStringProperty;

public class Exporter {
	
	private BufferedReader b;
	
	String noeudA;
	String lineA;

	public Exporter(){
		
	}
	
	public Graphe lireGraphe(File f) {
		try {
            b = new BufferedReader(new FileReader(f));
            String readLine = "";
            int controle = 1;
            int nbreNoeud = 0;
            Graphe g = new Graphe("", TypeGraphe.SIMPLE_N_O);
            
            while ((readLine = b.readLine()) != null) {
                if(controle == 1 && !readLine.trim().equals("@")) {
                	return null;
                }
                if(controle == 2) {
                	switch (readLine.trim()) {
	    				case "0":
	    					g.setTypeGraphe(TypeGraphe.SIMPLE_N_O);
	    					break;
	    				case "1":
	    					g.setTypeGraphe(TypeGraphe.PONDERE_N_O);
	    					break;
	    			default:
	    				g.setTypeGraphe(TypeGraphe.SIMPLE_N_O);
	    				break;
	    			}
                }
            	if(controle == 3) {
            		g.setNom(new SimpleStringProperty(readLine.trim()));
            	}
            	if(controle == 4) {
            		nbreNoeud = Integer.parseInt(readLine.trim());
            		if(nbreNoeud == 0) {
            			return g;
            		}
            	}
            	if(controle > 4 && controle <= nbreNoeud+4) {
            		g.creerNoeud(new Noeud(readLine.trim()));
            		Thread.sleep(0,5 * 1000);
            	}
            	if(controle > nbreNoeud+4) {
            		String[] parts = readLine.trim().split("@-@");
            		String part1 = parts[0];
            		String part2 = parts[1];
            		int part3 = Integer.parseInt(parts[2]);
            		g.getListeNoeud().get(g.getNoeudLabelToIndex(part1.trim())).ajouteVoisin(g.getNoeud(part2.trim()), part3);
            		g.getListeNoeud().get(g.getNoeudLabelToIndex(part2.trim())).ajouteVoisin(g.getNoeud(part1.trim()), part3);
            	}
            	controle++;
            }
            
            return g;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public void sauverGraphe(Graphe graphe, File sortie) {
		try {
			BufferedWriter buf = new BufferedWriter( 
					new OutputStreamWriter(
							new FileOutputStream( sortie ), Charset.forName("UTF-8").newEncoder()
							)
					);
			
			String line;
			
			buf.write("@\n");
			
			switch (graphe.getTypeGraphe()) {
				case SIMPLE_N_O:
					buf.write("0" + "\n");
					break;
				case PONDERE_N_O:
					buf.write("1" + "\n");
					break;
			default:
				buf.write("0" + "\n");
				break;
			}
			
			buf.write(graphe.getNom().get() + "\n");
			
			buf.write(String.valueOf(graphe.getNbNoeuds()) + "\n");
			
			for ( Noeud v : graphe.getListeNoeud() ) {
				line = v.getLabel().get() + "\n";
				buf.write(line);
			}
			for ( Noeud v : graphe.getListeNoeud() ) {
				if ( v.getNbVoisins() > 0 ) {
					for ( Noeud u : v.getSuccesseurs() ) {
						line = v.getLabel().get() + "@-@" + u.getLabel().get() + "@-@" + v.getPoidsUnSucesseur(u.getId()) + "\n";
						buf.write(line);
					}
				}
			}
			
			buf.close();
		}
		catch ( Exception e ) {			
			e.printStackTrace();
		}
	}
	
	public void exporterFichier(Graphe graphe, String nom) {
		toPNG(graphe, nom);
	}
	
	public void exporterArbre(Arbre graphe, String nom) {
		arbreToPNG(graphe, nom);
	}
	
	protected static File createFile(String fPath) {
		try {
			File f = new File(fPath);
			if ( f.exists() ) {
				f.delete();
			}
			//System.out.println("Ole Ole Ole " + f.createNewFile());
			f.createNewFile();
			
			//System.out.println("1.00001");
			//System.out.println("f null? " + (f == null));
			
			return f;
		}
		catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}
	
	// Get today's date in YYYYMMDD format, to be used as file prefix
	protected static String getDate() {
		ZonedDateTime t = ZonedDateTime.now();
		
		String str = "" + t.getYear() + twoDigits(t.getMonthValue()) + twoDigits(t.getDayOfMonth());
		return str;
	}
	
	protected static String getTime() {
		ZonedDateTime t = ZonedDateTime.now();
		String str;
		str = "" + t.getHour() + t.getMinute() + t.getSecond();
		return str;
	}
	
	// FILE MANIPULATION 
	private static String twoDigits(int n) {
		String str;
		if ( n < 10 ) {
			str = "0" + n;
		}
		else {
			str = "" + n;
		}
		return str;
	}
	
	// dessiner un png avec dot
	public void toPNG ( Graphe graphe, String fichier ) {
		try {
			File sortie = createFile(fichier);
			BufferedWriter buf = new BufferedWriter( 
					new OutputStreamWriter(
							new FileOutputStream( sortie ), Charset.forName("UTF-8").newEncoder()
							)
					);
			
			String line;
			String verifier = "";
			
			buf.write("graph " + graphe.getNom().get() + " {\n");
			
			for ( Noeud v : graphe.getListeNoeud() ) {
				line = "\t" + v.getLabel().get() + ";\n";
				buf.write(line);
				verifier += line;
			}
			for ( Noeud v : graphe.getListeNoeud() ) {
				if ( v.getNbVoisins() > 0 ) {
					for ( Noeud u : v.getSuccesseurs() ) {
						line = "\t" + v.getLabel().get() + " -- " + u.getLabel().get();
						if(graphe.getTypeGraphe() == TypeGraphe.PONDERE_N_O) {
							line += " [label="+v.getPoidsUnSucesseur(u.getId())+"];\n";
						} else {
							line += ";\n";
						}
						//on se rassure qu'on ne repete pas la ligne
						if(verifier.indexOf("\t" + u.getLabel().get() + " -- " + v.getLabel().get()) == -1) {
							buf.write(line);
							verifier += line;
						}
					}
				}
			}
			
			buf.write("}");
			
			buf.close();
 
			String commande = "neato -Tpng " + fichier + " -o " + fichier + ".png";
			Process process = Runtime.getRuntime().exec(commande);
			process.waitFor();
			//commande = "eog " + fichier + ".png &";
			//process = Runtime.getRuntime().exec(commande);
			//process.waitFor();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	// dessiner un png avec dot
	public void arbreToPNG ( Arbre arbre, String fichier ) {
		try {
			File sortie = createFile(fichier);
			BufferedWriter buf = new BufferedWriter( 
					new OutputStreamWriter(
							new FileOutputStream( sortie ), Charset.forName("UTF-8").newEncoder()
							)
					);
			
			lineA = "";
			noeudA = "";
			
			parcourArbre(arbre);
			
			buf.write("digraph " + arbre.getLabel() + " {\n");
			
			buf.write(noeudA);//System.out.println(noeudA);
			
			buf.write(lineA);
			
			buf.write("}");
			
			buf.close();
 
			String commande = "dot -Tpng " + fichier + " -o " + fichier + ".png";
			Process process = Runtime.getRuntime().exec(commande);
			process.waitFor();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void parcourArbre(Arbre a) {
		noeudA += a.getLabel() + "\n";
		if(a.nbEnfants() > 0) {
			for(Arbre b : a.getEnfants()) {
				lineA += "\t" + a.getLabel() + "->" + b.getLabel() + "\n";
				parcourArbre(b);
			}
		}
	}
}
