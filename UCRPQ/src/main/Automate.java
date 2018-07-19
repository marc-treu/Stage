package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Automate {
	
	// On débute toujours notre automate a 0;
	int etat_initial = 0;
	HashMap<Integer,List<Transition>> table_transition;
	int[] etat_final; 
	RegExp regexp;
	
	
	public Automate(HashMap<Integer,List<Transition>> table_transition, int [] etat_final, RegExp regexp) {
		this.table_transition = table_transition;
		this.etat_final = etat_final;
		this.regexp = regexp;
	}
	
	
	/**
	 * Algorithme de GLUSHKOV
	 * 
	 * 
	 * @param r, l'expression rationnel que l'on cherche a transformé en automate
	 * @return un Automate fini qui correspond au langage décrit par r
	 */
	public static Automate automateFromRegExp(RegExp r) {
		
		//INITIALISATION 
		
		List<Integer> finale = new ArrayList<>();
		HashMap<Integer,List<Transition>> tableTransition = new HashMap<Integer,List<Transition>>();
		RegExp r_renomer = r.getRename(1);
		
		
		List<Transition> temp_initaux = new ArrayList<>();
		// On recupere la liste des initiaux
		for (String e : r_renomer.getInitaux()) {
			temp_initaux.add(new Transition(Integer.parseInt(e.substring(1)),e.substring(0, 1)));
		}
		tableTransition.put(0, temp_initaux);
		
		
		// Boucle Principal
		for (String s : r_renomer.getEtiquette()) {
			List<String> sv = new ArrayList<>();
			if (!r_renomer.getSuivant(sv,s)) {
				finale.add(Integer.parseInt(s.substring(1)));
			}
			List<Transition> temp = new ArrayList<>();
			for (String e : sv) {
				temp.add(new Transition(Integer.parseInt(e.substring(1)),e.substring(0, 1)));
			}
			tableTransition.put(Integer.parseInt(s.substring(1)), temp);				
		}
		return  new Automate(tableTransition, finale.stream().mapToInt(Integer::intValue).toArray(),r);
	}
	
	
	public boolean isDeterminist() {
		Iterator<Integer> it = table_transition.keySet().iterator();
		while(it.hasNext()) {
			Set<String> teste = new HashSet<>();
			for(Transition t : table_transition.get(it.next())) {
				if(teste.contains(t.getEtiquette()))
					return false;
				teste.add(t.getEtiquette());
			}
		}
		return true;
	}
	
	public Automate getDeterminist() {
		
		if(this.isDeterminist()) // Si l'automate est deja deterministe
			return this;
		
		
		
		return null;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nRegExp :"+ this.regexp);
		sb.append("\ninitiaux : "+ this.etat_initial);
		sb.append("\nfinaux :"+ Arrays.toString(this.etat_final));
		
		sb.append("\n"+this.table_transition.toString());
		/*
		for(int i=0;i<this.table_transition.length;++i) {
			sb.append("\n");
			for (int j = 0; j<this.table_transition.length; ++j) {
				sb.append(this.table_transition[i][j]);
			}
		}*/
		return sb.toString();
	}
	
	
}
