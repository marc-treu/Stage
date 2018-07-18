package main;

import java.util.ArrayList;
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

		
		
		System.out.println("\nRegExp :"+ r_renomer);
		System.out.println("initiaux : "+ initiaux);
		System.out.println("finaux :"+finale);
		for(int i=0;i<taille;++i) {
			
			System.out.println(tableTransition[i]);
		}
		
		Automate automate = new Automate(tableTransition, finale.stream().mapToInt(Integer::intValue).toArray());

		
		return automate;
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
}
