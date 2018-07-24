package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Etat {

	String nom;
	boolean initial;
	boolean finale;
	List<Transition> transition;
	
	
	public Etat(String nom, boolean initial, boolean finale, List<Transition> transition) {
		this.nom = nom;
		this.initial = initial;
		this.finale = finale;
		this.transition = transition;
	}
	
	public Etat(String nom, boolean initial, boolean finale) {
		this.nom = nom;
		this.initial = initial;
		this.finale = finale;
		this.transition = new ArrayList<>();
	}
	
	public void addTransition(Transition t) {
		this.transition.add(t);
	}
	
	public boolean isDeterminist() {
		Set<String> test = new HashSet<>();
		for (Transition t : transition) {
			if (test.contains(t.getEtiquette()))
				return false;
			test.add(t.getEtiquette());
		}
		return true;
	}
	
	public List<String> getTransitionFromEtiquette(String etiquette){

		if (etiquette == "") {
			return Arrays.asList(this.getNom());
		}
		
		List<String> resultat = new ArrayList<>();

		for(Transition t : this.transition) {
			if(t.getEtiquette().equals(etiquette)) {
				resultat.add(t.getEtat());
			}
		}
		return resultat;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(initial ? (finale ? "IF" : "I " ) : (finale ? " F" : "  " ) );
		sb.append("|" +this.nom + ": " + this.transition);
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object nom) {
		return this.nom.equals(nom);
	}
	
	public Etat getEtat() {
		return this;
	}
	
	public List<String> getAlphabet() {
		List<String> resultat = new ArrayList<>();
		for (Transition t : this.transition) {
			resultat.add(t.getEtiquette());
		}
		return resultat;
	}
	
	public boolean isFinale() {
		return finale;
	}
	
	public boolean isInitial() {
		return initial;
	}
	
	public String getNom() {
		return nom;
	}
	
	public List<Transition> getTransition() {
		return transition;
	}

	public int compareTo(Etat e) {
		return this.nom.compareTo(e.nom);
	}
}
