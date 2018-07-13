package main;

import java.util.List;

public class Automate {
	
	// On débute toujours notre automate a 0;
	int etat_initial = 0;
	char [][] table_transition;
	int[] etat_final; 
	
	
	public Automate(char [][] table_transition, int [] etat_final) {
		this.table_transition = table_transition;
		this.etat_final = etat_final;
	}
	
	
	/**
	 * Algorithme de GLUSHKOV
	 * 
	 * 
	 * @param r, l'expreesion rationnel que l'on cherche a transformé en automate
	 * @return un Automate fini qui correspond au langage décrit par r
	 */
	public Automate automateFromRegExp(RegExp r) {
		
		//INITIALISATION 
		
		// On récuperer la taille de notre Expression
		int taille = r.getLength() + 1; 
		// On initialise notre tableau de transition avec des '0' partout
		char[][] tableTransition  = initialisationTableau(taille);
		RegExp r_renomer = r.getRename(0);
		
		List<String> initiaux = r_renomer.getInitaux();
		
		
		return null;
	}


	private char[][] initialisationTableau(int taille) {
		char [][] resulat= new char [taille][taille];
		for(int i=0;i<taille;i++) {
			for(int j=0;j<taille;j++) {
				resulat[i][j]='0';
			}
		}		
		return resulat;
	}
}
