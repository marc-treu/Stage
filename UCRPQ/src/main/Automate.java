package main;

public class Automate {
	
	// On débute toujours notre automate a 0;
	int etat_initial = 0;
	char [][] table_transition;
	int[] etat_final; 
	
	
	public Automate(int etat_initial,char [][] table_transition, int [] etat_final) {
		this.table_transition = table_transition;
		this.etat_final = etat_final;
	}
	
	
	public Automate automateFromRegExp(RegExp r) {
		
		
		
		return null;
	}

}
