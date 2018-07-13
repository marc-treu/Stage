package main;

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
		
		int taille = r.getLenght() + 1; 
		
		return null;
	}
}
