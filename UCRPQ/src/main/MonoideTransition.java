package main;

import java.util.HashMap;
import java.util.List;

public class MonoideTransition {


	HashMap<String,List<String>> mot;
	HashMap<List<String>,String> listeMot;
	HashMap<String,String> listeRegle;
	
	public MonoideTransition() {
		this.mot = new HashMap<>();
		this.listeMot = new HashMap<>();
		this.listeRegle = new HashMap<>();
	}
	
	public void ajoute(String mot, List<String> correspondant) {
		this.mot.put(mot,correspondant);
		this.listeMot.put(correspondant,mot);
	}
	
	public void ajoute(List<String> correspondant,String mot) {
		ajoute(mot, correspondant);
	}

	public boolean contient(List<String> correspond) {
		return this.listeMot.containsKey(correspond);
	}
	
	public boolean contient(String mot) {
		return this.mot.containsKey(mot);
		
	}
	

	public void ajouteRegle(String motlong, List<String> correspond) {
		this.listeRegle.put(motlong, listeMot.get(correspond));			
	}
	
	@Override
	public String toString() {
		return mot.toString()+"\n"+listeRegle.toString();
	}		

	
	
}
