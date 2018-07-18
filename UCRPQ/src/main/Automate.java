package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automate {
	
	// On débute toujours notre automate a 0;
	int etat_initial = 0;
	char [][] table_transition;
	int[] etat_final; 
	RegExp regexp;
	
	
	public Automate(char [][] table_transition, int [] etat_final, RegExp regexp) {
		this.table_transition = table_transition;
		this.etat_final = etat_final;
		this.regexp = regexp;
	}
	
	
	/**
	 * Algorithme de GLUSHKOV
	 * 
	 * 
	 * @param r, l'expreesion rationnel que l'on cherche a transformé en automate
	 * @return un Automate fini qui correspond au langage décrit par r
	 */
	public static Automate automateFromRegExp(RegExp r) {
		
		//INITIALISATION 
		
		List<Integer> finale = new ArrayList<>();
		// On récuperer la taille de notre Expression
		int taille = r.getLength() + 1; 
		// On initialise notre tableau de transition avec des '0' partout
		char[][] tableTransition  = initialisationTableau(taille);
		RegExp r_renomer = r.getRename(1);
		
		// On recupere la liste 
		List<String> initiaux = r_renomer.getInitaux();
		
		for(int i = 0; i<initiaux.size(); ++i) {
			tableTransition[0][Integer.parseInt(initiaux.get(i).substring(1))]
					= initiaux.get(i).charAt(0);					
		}
		
		
		// Boucle Principal
		
		for (String s : r_renomer.getEtiquette()) {
			
			List<String> sv = new ArrayList<>();
			if (!r_renomer.getSuivant(sv,s)) {
				finale.add(Integer.parseInt(s.substring(1)));
			}
			for (String suivant : sv) {
				tableTransition[Integer.parseInt(s.substring(1))]
						[Integer.parseInt(suivant.substring(1))]
								= suivant.charAt(0);					
			}
		}


		return  new Automate(tableTransition, finale.stream().mapToInt(Integer::intValue).toArray(),r);
	}


	private static char[][] initialisationTableau(int taille) {
		char [][] resulat= new char [taille][taille];
		for(int i=0;i<taille;i++) {
			for(int j=0;j<taille;j++) {
				resulat[i][j]='0';
			}
		}		
		return resulat;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nRegExp :"+ this.regexp);
		sb.append("\ninitiaux : "+ this.etat_initial);
		sb.append("\nfinaux :"+ Arrays.toString(this.etat_final));
		for(int i=0;i<this.table_transition.length;++i) {
			sb.append("\n");
			for (int j = 0; j<this.table_transition.length; ++j) {
				sb.append(this.table_transition[i][j]);
			}
		}
		return sb.toString();
	}
	
	
}
